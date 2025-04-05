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

    String outputStr = null;

    @Test
    void getMethodTest() {
        Assertions.assertTimeout(ofMinutes(2), () -> {
            try (final Socket socket = new Socket(HOST, PORT)) {
                System.out.println("Connected to server ..");
                try (final DataOutputStream output = new DataOutputStream(
                        new BufferedOutputStream(socket.getOutputStream()));
                        final DataInputStream input = new DataInputStream(
                                new BufferedInputStream(socket.getInputStream()));) {
                    final HttpRequest req = HttpRequest.builder()
                            .withMethod("GET").withUrlTarget("/srv/metrix")
                            .build();

                    output.write(req.toString().getBytes());
                    output.flush();

                    final byte[] buf = input.readAllBytes();
                    outputStr = new String(buf, 0, buf.length);
                }
            } catch (final IOException e) {
                System.out.println("Error Message - " + e.getMessage());
            }

            Assertions.assertNotNull(outputStr, "Http call output is null.");
            System.out.println("Http output [" + outputStr + "]");
            Assertions.assertAll("Http call output",
                    () -> Assertions.assertTrue(outputStr.contains("200 Ok"), "Missing 200 response"),
                    () -> Assertions.assertTrue(outputStr.contains("\"status\" : \"OK\"}"), "Missing status message"));
        });
    }

    @AfterAll
    static void tearDownAll() {
        server.stop();
    }

}
