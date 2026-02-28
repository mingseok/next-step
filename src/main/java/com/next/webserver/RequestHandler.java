package com.next.webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import com.next.webserver.domain.User;
import com.next.webserver.repository.DataBase;
import com.next.webserver.utils.HttpRequestUtils;
import com.next.webserver.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("새로운 유저 연결! 연결됨 IP : {}, Port : {}",
                connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream()
        ) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = br.readLine();
            String url = HttpRequestUtils.parseUrl(line);
            String method = line.split(" ")[0];
            log.debug("request url : {}", url);

            int contentLength = 0;
            while (line != null && !line.isEmpty()) {
                log.debug("request : {}", line);
                if (line.contains("Content-Length")) {
                    contentLength = Integer.parseInt(line.split(": ")[1].trim());
                }
                line = br.readLine();
            }

            DataOutputStream dos = new DataOutputStream(out);
            if ("POST".equals(method) && url.equals("/user/create")) {
                String body = IOUtils.readData(br, contentLength);
                Map<String, String> params = HttpRequestUtils.parseQueryString(body);
                User user = new User(
                        params.get("userId"),
                        params.get("password"),
                        params.get("name"),
                        params.get("email")
                );
                DataBase.addUser(user);
                log.debug("user : {}", user);
                response302Header(dos, "/index.html");
                return;
            }

            if ("POST".equals(method) && url.equals("/user/login")) {
                String body = IOUtils.readData(br, contentLength);
                Map<String, String> params = HttpRequestUtils.parseQueryString(body);
                User user = DataBase.findUserById(params.get("userId"));

                if (user != null && user.getPassword().equals(params.get("password"))) {
                    response302LoginHeader(dos, "/index.html", "logined=true");
                } else {
                    response302LoginHeader(dos, "/user/login_failed.html", "logined=false");
                }
                return;
            }

            String filePath = "./webapp" + url;
            File file = new File(filePath);
            Path path = file.toPath();
            byte[] body = Files.readAllBytes(path);

            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302LoginHeader(DataOutputStream dos, String location, String cookie) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + location + "\r\n");
            dos.writeBytes("Set-Cookie: " + cookie + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String location) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + location + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
