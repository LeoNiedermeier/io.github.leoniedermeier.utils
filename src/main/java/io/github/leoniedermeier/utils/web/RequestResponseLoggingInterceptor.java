package io.github.leoniedermeier.utils.web;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

public class RequestResponseLoggingInterceptor implements ClientHttpRequestInterceptor {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        log(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        log(response);
        return response;
    }

    private void log(HttpRequest request, byte[] body) throws IOException {
        if (log.isDebugEnabled()) {
            // @formatter:off
            String message =
              "\n======================= request begin ===========================================" +
              "\nURI         :  " + request.getURI() + 
              "\nMethod      : " + request.getMethod() + 
              "\nHeaders     : " + request.getHeaders() + 
              "\nRequest body: " + new String(body, UTF_8) + 
              "\n======================= request end =============================================";
            // @formatter:on
            log.debug(message);
        }
    }

    private void log(ClientHttpResponse response) throws IOException {
        if (log.isDebugEnabled()) {
            // @formatter:off
            String message =
              "\n======================= response begin ==========================================" +
              "\nStatus code  : " + response.getStatusCode() +
              "\nStatus text  : " + response.getStatusText() +
              "\nHeaders      : " + response.getHeaders() +
              "\nResponse body: " + StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()) + 
              "\n======================= response end ============================================";
            // @formatter:on
            log.debug(message);
        }
    }
}