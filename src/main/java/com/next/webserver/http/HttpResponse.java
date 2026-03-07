package com.next.webserver.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);

    private final DataOutputStream dos;
    private final Map<String, String> headers = new HashMap<>();

    public HttpResponse(OutputStream out) {
        dos = new DataOutputStream(out);
    }

    public void addHeader(String name, String value) {
        headers.put(name, value);
    }

    public void forward(String url) {
        try {
            byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
            String contentType = resolveContentType(url);
            response200Header(body.length, contentType);
            responseBody(body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void forwardBody(String html) {
        byte[] body = html.getBytes();
        response200Header(body.length, "text/html");
        responseBody(body);
    }

    public void sendRedirect(String location) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + location + "\r\n");
            writeHeaders();
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(int contentLength, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + contentLength + "\r\n");
            writeHeaders();
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void writeHeaders() {
        try {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                dos.writeBytes(entry.getKey() + ": " + entry.getValue() + "\r\n");
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private String resolveContentType(String url) {
        if (url.endsWith(".css")) {
            return "text/css";
        }

        if (url.endsWith(".js")) {
            return "application/javascript";
        }
        return "text/html";
    }
}
