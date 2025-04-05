package co.in.nnj.learn.server.http;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HttpRequestTest {

    @BeforeEach
    void init() {
        System.out.println("Per test init");
    }

    @AfterEach
    void tearDown() {
        System.out.println("Per test clenup");
    }

    @Test
    public void httpGetRequestTest() {
        final HttpRequest req = HttpRequest.builder()
                .withMethod("GET")
                .withHeader("header1", "value1")
                .withHeader("header2", "value2")
                .withUrlTarget("http://google.co.in")
                .withData("client=any").build();

        Assertions.assertEquals(req.getData(), "client=any");

        final String reqStr = req.toString();
        Assertions.assertNotNull(reqStr);
        System.out.println("Http Request [" + reqStr + "]");

        /*
         * Assertions.assertTrue(reqStr.contains("GET"));
         * Assertions.assertTrue(reqStr.contains("http://google.co.in"));
         * Assertions.assertFalse(reqStr.contains("client=any"));
         */

        Assertions.assertAll("HttpRequest.toString() output",
                () -> Assertions.assertTrue(reqStr.contains("GET"), "Http method missing"),
                () -> Assertions.assertTrue(reqStr.contains("http://google.co.in"), "Url missing"),
                () -> Assertions.assertFalse(reqStr.contains("client=any"), "Contains \"client=any\""));
    }

    @Test
    public void httpPostRequestTest() {
        final HttpRequest req = HttpRequest.builder()
                .withMethod("POST")
                .withHeader("header1", "value1")
                .withHeader("header2", "value2")
                .withUrlTarget("http://google.co.in")
                .withData("client=any").build();

        final String reqStr = req.toString();
        Assertions.assertNotNull(reqStr);
        System.out.println("Http Request [" + reqStr + "]");
        Assertions.assertAll("HttpRequest.toString() output",
                () -> Assertions.assertTrue(reqStr.contains("POST"), "Http method missing"),
                () -> Assertions.assertTrue(reqStr.contains("http://google.co.in"), "Url missing"),
                () -> Assertions.assertTrue(reqStr.contains("client=any"), "Data missing"));
    }
}
