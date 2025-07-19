package co.in.nnj.learn.jee.persi.rel;

import java.net.URL;
import java.net.http.HttpClient;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(ArquillianExtension.class)
public class ControllerTest {

    final Logger LOGGER = LoggerFactory.getLogger(ControllerTest.class.getName());

    @ArquillianResource
    private URL baseUrl;

    static {
        System.setProperty("tomee.version", "10.1.0");
        System.setProperty("tomee.classifier", "plus");
    }

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "myapp.war")
                .addPackages(true, "co.in.nnj.learn.jee")
                .addPackages(true, "org.hsqldb")
                .addAsResource("persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource("resources.xml", "resources.xml")
                .addAsWebInfResource("beans.xml", "beans.xml");
    }

    @Test
    public void cartItemCRUDShouldWork() {
        try {
            final HttpClient httpClient = HttpClient.newHttpClient();
            final CartRestClient cartClient = new CartRestClient(baseUrl, httpClient);
            // -- C => Create Cart
            final String cartId = cartClient.cartCreateTest();

            // -- R => Read Cart
            cartClient.cartReadTest(cartId);

            // -- C => Create items
            final String itemId1 = cartClient.cartAddItemTest(cartId, "item1");
            final String itemId2 = cartClient.cartAddItemTest(cartId, "item2");

            // -- R => Read Items
            final JsonArray items = cartClient.getItemsTest(cartId);
            assertEquals(2, items.size());
            for (final JsonValue value : items) {
                final JsonObject obj = value.asJsonObject();
                final String id = obj.getString("id");
                final String title = obj.getString("title");
                if (id.equals(itemId1)) {
                    assertEquals(title, "item1");
                } else if (id.equals(itemId2)) {
                    assertEquals(title, "item2");
                } else {
                    assertFalse(true, "Failed to retrieve items!");
                    break;
                }
            }

        } catch (final Exception e) {
            LOGGER.info(e.getMessage());
            assertFalse(true);
        }
    }
}
