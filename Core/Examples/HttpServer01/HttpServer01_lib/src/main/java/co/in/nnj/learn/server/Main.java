package co.in.nnj.learn.server;

import java.io.IOException;

import co.in.nnj.learn.server.http.HttpResponse;
import co.in.nnj.learn.server.http.HttpResponseCode;
import co.in.nnj.learn.server.http.SimpleHttpServer;

public class Main {
    public static void main(final String[] args) {
        final SimpleHttpServer httpSimpleServer = SimpleHttpServer.builder()
                .withPort(5000)
                .withRoute("/srv/metrix", (req, respBuilder) -> {
                    final String metrix = "{\"number\": 10, \"address\": \"any-thing\"}";
                    final HttpResponse httpResponse = respBuilder.withResponseCode(200)
                            .withData(metrix).build();
                    try {
                        httpResponse.send();
                    } catch (final IOException e) {
                    }
                }).withRoute("/srv/shutdown", (req, respBuilder) -> {
                    final HttpResponse resp = respBuilder.withResponseCode(HttpResponseCode.OK)
                            .withData("Requested shutdown!").build();
                    try {
                        resp.send();
                    } catch (final IOException e) {
                    }
                }).build();

        httpSimpleServer.start();
    }
}
