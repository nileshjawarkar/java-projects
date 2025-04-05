package co.in.nnj.learn.server.http;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HttpRequestTest {

    @Test
    public void httpRequestBuildTest() {
        final HttpRequest req = HttpRequest.builder()
                .withMethod("GET")
                .withHeader("header1", "value1")
                .withHeader("header2", "value2")
                .withUrlTarget("http://google.co.in")
                .withData("client=any").build();

        Assertions.assertEquals(req.getData(), "client=any");
        final String reqStr = req.toString();
        System.out.println("Http Request [" + reqStr + "]");
        Assertions.assertEquals(true, reqStr.contains("GET"));
        Assertions.assertEquals(true, reqStr.contains("http://google.co.in"));
        Assertions.assertEquals(false, reqStr.contains("client=any"));
    }
}
