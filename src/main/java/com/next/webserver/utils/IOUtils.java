package com.next.webserver.utils;

import java.io.BufferedReader;
import java.io.IOException;

public class IOUtils {
    public static String readData(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return new String(body);
    }
}