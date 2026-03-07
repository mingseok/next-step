package com.next.webserver.http;

import com.next.webserver.utils.HttpRequestUtils;
import com.next.webserver.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpRequest {

    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private String method;
    private String url;
    private Map<String, String> params;
    private final List<String> headers;

    public HttpRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        parseRequestLine(br.readLine());
        headers = readHeaders(br);
        params = parseParams(br);
    }

    private void parseRequestLine(String requestLine) {
        String[] tokens = requestLine.split(" ");
        method = tokens[0];
        url = tokens[1];
        log.debug("request url : {}", url);

        if ("GET".equals(method) && url.contains("?")) {
            int index = url.indexOf("?");
            params = HttpRequestUtils.parseQueryString(url.substring(index + 1));
            url = url.substring(0, index);
        }
    }

    private List<String> readHeaders(BufferedReader br) throws IOException {
        List<String> headers = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null && !line.isEmpty()) {
            log.debug("request : {}", line);
            headers.add(line);
        }
        return headers;
    }

    private Map<String, String> parseParams(BufferedReader br) throws IOException {
        if ("POST".equals(method)) {
            int contentLength = getContentLength(headers);
            String body = IOUtils.readData(br, contentLength);
            return HttpRequestUtils.parseQueryString(body);
        }
        return params;
    }

    private int getContentLength(List<String> headers) {
        return headers.stream()
                .filter(line -> line.contains("Content-Length"))
                .mapToInt(line -> Integer.parseInt(line.split(": ")[1].trim()))
                .findFirst()
                .orElse(0);
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getHeader(String name) {
        return headers.stream()
                .filter(line -> line.startsWith(name + ":"))
                .map(line -> line.split(": ", 2)[1].trim())
                .findFirst()
                .orElse("");
    }

    public String getParameter(String name) {
        if (params == null) {
            return null;
        }
        return params.get(name);
    }
}
