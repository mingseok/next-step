package com.next.webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServer {

    private static final Logger log = LoggerFactory.getLogger(WebServer.class);
    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) {
        int port;
        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }

        try (ServerSocket listenSocket = new ServerSocket(port)) {
            log.info("웹 애플리케이션 서버가 시작되었습니다. {} port.", port);

            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                RequestHandler requestHandler = new RequestHandler(connection);
                requestHandler.start();
            }
        } catch (IOException e) {
            log.error("서버 시작 실패 : {}", e.getMessage());
        }
    }
}
