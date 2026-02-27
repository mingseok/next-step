package com.next.webserver.utils;

import java.util.HashMap;
import java.util.Map;

public class HttpRequestUtils {

    public static String parseUrl(String requestLine) {
        String[] tokens = requestLine.split(" ");
        return tokens[1];
    }

    public static Map<String, String> parseQueryString(String queryString) {
        Map<String, String> params = new HashMap<>();
        String[] tokens = queryString.split("&");
        for(String token : tokens) {
            String[] pair = token.split("=");
            if(pair.length == 2) {
                params.put(pair[0], pair[1]);
            }
        }
        return params;
    }
}
