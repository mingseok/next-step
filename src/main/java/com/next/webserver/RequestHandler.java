package com.next.webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

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
            log.debug("request url : {}", url);

            while (line != null && !line.isEmpty()) {
                log.debug("request : {}", line);
                line = br.readLine();
            }

            if (url.contains("?")) {
                int index = url.indexOf("?");
                String requestPath = url.substring(0, index);
                String queryString = url.substring(index + 1);

                Map<String, String> params = HttpRequestUtils.parseQueryString(queryString);
                User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
                log.debug("user : {}", user);

                url = requestPath;
            }

            String filePath = "./webapp" + url;
            File file = new File(filePath);
            Path path = file.toPath();
            byte[] body = Files.readAllBytes(path);

            DataOutputStream dos = new DataOutputStream(out);
            response200Header(dos, body.length);
            responseBody(dos, body);
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
