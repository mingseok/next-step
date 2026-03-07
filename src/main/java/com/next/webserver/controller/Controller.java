package com.next.webserver.controller;

import com.next.webserver.http.HttpRequest;
import com.next.webserver.http.HttpResponse;

public interface Controller {

    void service(HttpRequest request, HttpResponse response);
}
