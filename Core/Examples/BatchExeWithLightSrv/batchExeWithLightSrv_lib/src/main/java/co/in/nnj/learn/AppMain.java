package co.in.nnj.learn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.in.nnj.learn.core.AppController;

public class AppMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppMain.class.getName());

    public static void main(final String[] args) {
        final AppController app = new AppController();
        if (app.setup()) {
            LOGGER.info("App starting ...");
            app.start();
        } else {
            LOGGER.error("Failed to start app ...");
        }
    }
}
