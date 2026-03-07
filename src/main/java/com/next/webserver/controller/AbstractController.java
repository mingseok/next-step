package com.next.webserver.controller;

import com.next.webserver.http.HttpRequest;
import com.next.webserver.http.HttpResponse;

public abstract class AbstractController implements Controller {

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        if ("POST".equals(request.getMethod())) {
            doPost(request, response);
            return;
        }
        doGet(request, response);
    }

    public void doGet(HttpRequest request, HttpResponse response) {}

    public void doPost(HttpRequest request, HttpResponse response) {}
}
