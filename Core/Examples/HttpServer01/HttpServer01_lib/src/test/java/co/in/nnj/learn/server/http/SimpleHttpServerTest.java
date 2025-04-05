package co.in.nnj.learn.server.http;

import static java.time.Duration.ofMinutes;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class SimpleHttpServerTest {
    static SimpleHttpServer server;
    private static final String HOST = "localhost";
    private static final int PORT = 5000;

    private static int count = 0;

    @BeforeAll
    static void initAll() {
        server = SimpleHttpServer.builder()
                .withPort(PORT)
                .withRoute("/srv/metrix", (req, respBuilder) -> {
                    try {
                        respBuilder.withData("{\"status\" : \"OK\"}")
                                .withResponseCode(HttpResponseCode.OK)
                                .build().send();
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                }).withRoute("/srv/inccount", (req, respBuilder) -> {
                    try {
                        final String method = req.getMethod();
                        if (!"POST".equals(method)) {
                            respBuilder.withResponseCode(HttpResponseCode.BAD_REQUEST)
                                    .build().send();
                            return;
                        }

                        synchronized (SimpleHttpServerTest.class) {
                            count++;
                        }
                        respBuilder.withData("{\"count\" : " + count + "}")
                                .withResponseCode(HttpResponseCode.OK)
                                .build().send();
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                }).build();
        new Thread(server).start();
    }

    HttpResponse executeMethod(final String method, final String url, final String data) {
        try (final Socket socket = new Socket(HOST, PORT)) {
            System.out.println("Connected to server ..");
            try (final DataOutputStream output = new DataOutputStream(
                    new BufferedOutputStream(socket.getOutputStream()));
                    final DataInputStream input = new DataInputStream(
                            new BufferedInputStream(socket.getInputStream()));) {
                final HttpRequest req = HttpRequest.builder()
                        .withMethod(method).withUrlTarget(url)
                        .withData(data)
                        .build();

                output.write(req.toString().getBytes());
                output.flush();
                final byte[] buf = input.readAllBytes();

                return HttpResponse.fromString(new String(buf, 0, buf.length));
            }
        } catch (final IOException e) {
            System.out.println("Error Message - " + e.getMessage());
        }
        return HttpResponse.builder().withResponseCode(HttpResponseCode.SERVICE_UNAVAILABLE).build();
    }

    @Test
    void getMethodTest() {
        Assertions.assertTimeout(ofMinutes(2), () -> {
            final HttpResponse response = executeMethod("GET", "/srv/metrix", null);
            Assertions.assertNotNull(response, "Http call output is null.");
            System.out.println("Output [" + response.toString() + "]");
            Assertions.assertAll("Http get output",
                    () -> Assertions.assertTrue(response.getStatusCode() == HttpResponseCode.OK,
                            "Expeted 200 response"),
                    () -> Assertions.assertTrue(response.getData() != null, "Expeted response with status"),
                    () -> Assertions.assertTrue(response.getData().contains("\"status\" : \"OK\"}"),
                            "Expeted status message"));
        });
    }

    @Test
    void postMethodTest() {
        Assertions.assertTimeout(ofMinutes(2), () -> {
            executeMethod("POST", "/srv/inccount", null);
            final HttpResponse response = executeMethod("POST", "/srv/inccount", null);
            Assertions.assertNotNull(response, "Http call output is null.");
            System.out.println("Output [" + response.toString() + "]");
            final String data = response.getData();
            Assertions.assertAll("Http post output",
                    () -> Assertions.assertEquals(response.getStatusCode(), HttpResponseCode.OK,
                            "Expected 200 response code"),
                    () -> Assertions.assertNotNull(data, "Expected response with count"),
                    () -> Assertions.assertEquals("{\"count\" : 2}", data, "Expected response with count"));
        });
    }

    @Test
    void getNotSupportedTest() {
        Assertions.assertTimeout(ofMinutes(2), () -> {
            final HttpResponse response = executeMethod("GET", "/srv/inccount", null);
            Assertions.assertNotNull(response, "Http call output is null.");
            System.out.println("Output [" + response.toString() + "]");

            Assertions.assertEquals(response.getStatusCode(), HttpResponseCode.BAD_REQUEST,
                    "Expected BAD_REQUEST response code");
        });
    }

    @AfterAll
    static void tearDownAll() {
        server.stop();
    }
}
