package co.in.nnj.learn.jee.persi.rel;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CartRestClient {
    final Logger LOGGER = LoggerFactory.getLogger(CartRestClient.class.getName());
    private final String strBaseURI;
    private final HttpClient httpClient;

    public CartRestClient(final URL baseUrl, final HttpClient httpClient) throws URISyntaxException {
        this.strBaseURI = URI.create(baseUrl.toURI().toString() + "api/cart").toString();
        LOGGER.info("service url - {}", strBaseURI);
        this.httpClient = httpClient;
    }

    public String cartCreateTest()
            throws URISyntaxException, IOException, InterruptedException {
        final String strCart = Json.createObjectBuilder()
                .add("owner", "user01")
                .build().toString();

        final HttpRequest createReq = HttpRequest.newBuilder(URI.create(strBaseURI))
                .header("content-type", "application/json")
                .header("accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(strCart)).build();

        final HttpResponse<String> createResp = httpClient.send(createReq, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, createResp.statusCode());
        final String createBody = createResp.body();
        assertNotNull(createBody);
        LOGGER.info("create response = [{}]", createBody);

        String cartId = null;
        try (JsonReader reader = Json.createReader(new StringReader(createBody));) {
            cartId = reader.readObject().getString("id");
        }

        assertNotNull("Failed to retrieve cart-id from creates response!", cartId);
        LOGGER.info("cart-id = [{}]", cartId);
        return cartId;
    }

    public void cartReadTest(final String cartId) throws URISyntaxException, IOException, InterruptedException {

        final URI uriGet = URI.create(String.format("%s/%s", strBaseURI, cartId));
        LOGGER.info("cart get url[{}]", uriGet.toString());

        final HttpRequest readReq = HttpRequest.newBuilder(uriGet)
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

    public String cartAddItemTest(final String cartId, final String title) throws IOException, InterruptedException {
        final URI uriPostItem = URI.create(String.format("%s/%s/item", strBaseURI, cartId));
        LOGGER.info("cart add item url[{}]", uriPostItem.toString());

        final String strCartItem = Json.createObjectBuilder()
                .add("title", title)
                .build().toString();

        final HttpRequest createItemReq = HttpRequest.newBuilder(uriPostItem)
                .header("content-type", "application/json")
                .header("accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(strCartItem)).build();

        final HttpResponse<String> createItemResp = httpClient.send(createItemReq, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, createItemResp.statusCode());
        final String createItemBody = createItemResp.body();
        assertNotNull(createItemBody);

        String itemId = null;
        LOGGER.info(String.format("read response = [%s]", createItemBody));
        try (JsonReader reader = Json.createReader(new StringReader(createItemBody));) {
            final JsonObject obj = reader.readObject();
            assertTrue(obj.containsKey("id"));
            assertTrue(obj.containsKey("title"));
            itemId = obj.getString("id");
        }
        return itemId;
    }

    public JsonArray getItemsTest(final String cartId) throws IOException, InterruptedException {
        final URI uriGetItem = URI.create(String.format("%s/%s/item", strBaseURI, cartId));
        LOGGER.info("cart add item url[{}]", uriGetItem.toString());

        final HttpRequest readItemReq = HttpRequest.newBuilder(uriGetItem)
                .header("content-type", "application/json")
                .header("accept", "application/json")
                .GET().build();

        final HttpResponse<String> readItemResp = httpClient.send(readItemReq, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, readItemResp.statusCode());
        final String readItemBody = readItemResp.body();
        assertNotNull(readItemBody);

        LOGGER.info(String.format("read item response = [%s]", readItemBody));
        try (JsonReader reader = Json.createReader(new StringReader(readItemBody));) {
            return reader.readArray();
        }
    }

}
