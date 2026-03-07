package com.next.webserver.controller;

import com.next.webserver.domain.User;
import com.next.webserver.http.HttpRequest;
import com.next.webserver.http.HttpResponse;
import com.next.webserver.repository.DataBase;

public class LoginController extends AbstractController {

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        User user = DataBase.findUserById(request.getParameter("userId"));
        boolean loginSuccess = user != null && user.matchPassword(request.getParameter("password"));

        if (loginSuccess) {
            response.addHeader("Set-Cookie", "logined=true");
            response.sendRedirect("/index.html");
            return;
        }
        response.addHeader("Set-Cookie", "logined=false");
        response.sendRedirect("/user/login_failed.html");
    }
}
