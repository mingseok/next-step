package com.next.webserver;

public class HttpRequestUtils {

    public static String parseUrl(String requestLine) {
        String[] tokens = requestLine.split(" ");
        return tokens[1];
    }
}
