package com.next.webserver;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class HttpRequestUtilsTest {

    @Test
    void parseUrl() {
        String requestLine = "GET /index.html HTTP/1.1";
        assertThat(HttpRequestUtils.parseUrl(requestLine)).isEqualTo("/index.html");
    }

    @Test
    void parseQueryString() {
        String queryString = "userId=javajigi&password=password&name=JaeSung&email=javajigi%40slipp.net";
        Map<String, String> params = HttpRequestUtils.parseQueryString(queryString);

        assertThat(params.get("userId")).isEqualTo("javajigi");
        assertThat(params.get("password")).isEqualTo("password");
        assertThat(params.get("name")).isEqualTo("JaeSung");
    }
}
