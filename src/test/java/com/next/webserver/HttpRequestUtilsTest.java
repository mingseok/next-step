package com.next.webserver;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HttpRequestUtilsTest {

    @Test
    void parseUrl() {
        String requestLine = "GET /index.html HTTP/1.1";
        assertThat(HttpRequestUtils.parseUrl(requestLine)).isEqualTo("/index.html");
    }
}
