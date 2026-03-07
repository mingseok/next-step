package com.next.webserver.controller;

import com.next.webserver.domain.User;
import com.next.webserver.http.HttpRequest;
import com.next.webserver.http.HttpResponse;
import com.next.webserver.repository.DataBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateUserController extends AbstractController {

    private static final Logger log = LoggerFactory.getLogger(CreateUserController.class);

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        User user = new User(
                request.getParameter("userId"),
                request.getParameter("password"),
                request.getParameter("name"),
                request.getParameter("email")
        );
        DataBase.addUser(user);
        log.debug("user : {}", user);
        response.sendRedirect("/index.html");
    }
}
