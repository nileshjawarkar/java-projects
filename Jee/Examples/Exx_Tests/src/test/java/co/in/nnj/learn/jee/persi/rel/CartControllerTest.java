package co.in.nnj.learn.jee.persi.rel;

import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Logger;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(ArquillianExtension.class)
public class CartControllerTest {

    final Logger LOGGER = Logger.getLogger(CartControllerTest.class.getName());

    @ArquillianResource
    private URL baseUrl;

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "myapp.war")
                .addPackages(true, "co.in.nnj.learn.jee")
                .addPackages(true, "org.hsqldb")
                .addAsResource("persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource("resources.xml", "resources.xml")
                .addAsWebInfResource("beans.xml", "beans.xml");
    }

    public String cartCreateTest(final HttpClient httpClient)
            throws URISyntaxException, IOException, InterruptedException {
        final String strCart = Json.createObjectBuilder()
                .add("owner", "user01")
                .build().toString();

        final HttpRequest createReq = HttpRequest.newBuilder(baseUrl.toURI().resolve("/myapp/api/cart"))
                .header("content-type", "application/json")
                .header("accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(strCart)).build();

        final HttpResponse<String> createResp = httpClient.send(createReq, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, createResp.statusCode());
        final String createBody = createResp.body();
        assertNotNull(createBody);
        LOGGER.info(String.format("create response = [%s]", createBody));

        String cartId;
        try (JsonReader reader = Json.createReader(new StringReader(createBody));) {
            cartId = reader.readObject().getString("id");
        }

        assertNotNull("Failed to retrieve cart-id from creates response!", cartId);
        LOGGER.info(String.format("cart-id = [%s]", cartId));
        return cartId;
    }

    public void cartReadTest(final String cartId, final HttpClient httpClient)
            throws URISyntaxException, IOException, InterruptedException {

        final HttpRequest readReq = HttpRequest.newBuilder(baseUrl.toURI().resolve("/myapp/api/cart/" + cartId))
                .header("content-type", "application/json")
                .header("accept", "application/json")
                .GET().build();

        final HttpResponse<String> readResp = httpClient.send(readReq, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, readResp.statusCode());
        final String readBody = readResp.body();
        assertNotNull(readBody);

        LOGGER.info(String.format("read response = [%s]", readBody));
        try (JsonReader reader = Json.createReader(new StringReader(readBody));) {
            final JsonObject obj = reader.readObject();
            final String readCartId = obj.getString("id");
            assertEquals(cartId, readCartId);
            final String readCartOwner = obj.getString("owner");
            assertEquals("user01", readCartOwner);
        }
    }

    @Test
    public void cartCRUDShouldWork() {
        try {
            final HttpClient httpClient = HttpClient.newHttpClient();
            final String cartId = cartCreateTest(httpClient);
            cartReadTest(cartId, httpClient);
        } catch (final Exception e) {
            LOGGER.info(e.getMessage());
            assertFalse(true);
        }
    }
}
