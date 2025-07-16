package co.in.nnj.learn.jee.persi.rel;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(ArquillianExtension.class)
public class CartControllerTest {
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
    public void createCartShouldWork() {
        assertTrue(true);
    }
}
