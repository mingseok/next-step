package com.next.webserver;

import java.io.*;
import java.net.Socket;

import com.next.webserver.domain.User;
import com.next.webserver.http.HttpRequest;
import com.next.webserver.http.HttpResponse;
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
            HttpResponse response = new HttpResponse(out);
            String method = request.getMethod();
            String url = request.getUrl();

            if ("POST".equals(method) && url.equals("/user/create")) {
                User user = new User(
                        request.getParameter("userId"),
                        request.getParameter("password"),
                        request.getParameter("name"),
                        request.getParameter("email")
                );
                DataBase.addUser(user);
                log.debug("user : {}", user);
                response.sendRedirect("/index.html");
                return;
            }

            if ("POST".equals(method) && url.equals("/user/login")) {
                User user = DataBase.findUserById(request.getParameter("userId"));
                if (user != null && user.getPassword().equals(request.getParameter("password"))) {
                    response.addHeader("Set-Cookie", "logined=true");
                    response.sendRedirect("/index.html");
                } else {
                    response.addHeader("Set-Cookie", "logined=false");
                    response.sendRedirect("/user/login_failed.html");
                }
                return;
            }

            if (url.equals("/user/list")) {
                if (!("logined=true".equals(request.getHeader("Cookie")))) {
                    response.sendRedirect("/user/login.html");
                    return;
                }
                response.forwardBody(makeUserListHtml());
                return;
            }

            response.forward(url);
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
}
