package com.next.webserver;

import com.next.webserver.controller.Controller;
import com.next.webserver.controller.CreateUserController;
import com.next.webserver.controller.ListUserController;
import com.next.webserver.controller.LoginController;
import com.next.webserver.http.HttpRequest;
import com.next.webserver.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class RequestHandler extends Thread {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final Map<String, Controller> controllers = new HashMap<>();

    static {
        controllers.put("/user/create", new CreateUserController());
        controllers.put("/user/login", new LoginController());
        controllers.put("/user/list", new ListUserController());
    }

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

            Controller controller = controllers.get(request.getUrl());
            if (controller != null) {
                controller.service(request, response);
                return;
            }

            response.forward(request.getUrl());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
