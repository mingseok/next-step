package com.next.webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

import com.next.webserver.domain.User;
import com.next.webserver.http.HttpRequest;
import com.next.webserver.repository.DataBase;
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
            HttpRequest request = new HttpRequest(in);
            String method = request.getMethod();
            String url = request.getUrl();

            DataOutputStream dos = new DataOutputStream(out);

            if ("POST".equals(method) && url.equals("/user/create")) {
                User user = new User(
                        request.getParameter("userId"),
                        request.getParameter("password"),
                        request.getParameter("name"),
                        request.getParameter("email")
                );
                DataBase.addUser(user);
                log.debug("user : {}", user);
                response302Header(dos, "/index.html");
                return;
            }

            if ("POST".equals(method) && url.equals("/user/login")) {
                User user = DataBase.findUserById(request.getParameter("userId"));
                if (user != null && user.getPassword().equals(request.getParameter("password"))) {
                    response302LoginHeader(dos, "/index.html", "logined=true");
                } else {
                    response302LoginHeader(dos, "/user/login_failed.html", "logined=false");
                }
                return;
            }

            if (url.equals("/user/list")) {
                if (!("logined=true".equals(request.getHeader("Cookie")))) {
                    response302Header(dos, "/user/login.html");
                    return;
                }
                byte[] body = makeUserListHtml().getBytes();
                response200Header(dos, body.length, "text/html");
                responseBody(dos, body);
                return;
            }

            byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
            String contentType = url.endsWith(".css") ? "text/css" : "text/html";
            response200Header(dos, body.length, contentType);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private String makeUserListHtml() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head><link rel='stylesheet' href='/css/style.css'></head><body><table>");
        for (User user : DataBase.findAll()) {
            sb.append("<tr>");
            sb.append("<td>").append(user.getUserId()).append("</td>");
            sb.append("<td>").append(user.getName()).append("</td>");
            sb.append("<td>").append(user.getEmail()).append("</td>");
            sb.append("</tr>");
        }
        sb.append("</table></body></html>");
        return sb.toString();
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

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
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
