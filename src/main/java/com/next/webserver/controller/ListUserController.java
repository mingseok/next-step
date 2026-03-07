package com.next.webserver.controller;

import com.next.webserver.domain.User;
import com.next.webserver.http.HttpRequest;
import com.next.webserver.http.HttpResponse;
import com.next.webserver.repository.DataBase;

public class ListUserController extends AbstractController {

    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        if (!isLogin(request.getHeader("Cookie"))) {
            response.sendRedirect("/user/login.html");
            return;
        }
        response.forwardBody(makeUserListHtml());
    }

    private boolean isLogin(String cookie) {
        return "logined=true".equals(cookie);
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
