package co.in.nnj.learn.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import co.in.nnj.learn.core.AppStatus;
import co.in.nnj.learn.util.ByteArrayConverter;

public class Client {
    // -- private static final Logger LOGGER =
    // LoggerFactory.getLogger(Client.class.getName());
    public AppStatus getAppStatus() {
        try (final Socket socket = new Socket("localhost", 5000)) {
            try (final OutputStream outputStream = socket.getOutputStream();) {
                // -- Connvert object to byte array & write to socket
                final byte[] byteArray =
                    ByteArrayConverter.toByteArray(ServerRequest.REQ_SHARE_METRIX);
                outputStream.write(byteArray);
                outputStream.flush();

                try (
                    final ObjectInputStream sin = new ObjectInputStream(socket.getInputStream());) {
                    final AppStatus appStatus = (AppStatus) sin.readObject();
                    return appStatus;
                } catch (final ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (final IOException e) {
            // -- LOGGER.error("Failed to get app-status from app-main", e);
            e.printStackTrace();
        }
        return null;
    }

    public boolean requestShutdown() {
        try (final Socket socket = new Socket("localhost", 5000)) {
            try (final OutputStream outputStream = socket.getOutputStream();) {
                // -- Connvert object to byte array
                final byte[] byteArray =
                    ByteArrayConverter.toByteArray(ServerRequest.REQ_M_LIFECYCLE_PRERESTART);
                outputStream.write(byteArray);
                outputStream.flush();

                try (final BufferedReader reader =
                         new BufferedReader(new InputStreamReader(socket.getInputStream()));) {
                    final String line = reader.readLine();
                    System.out.println(line);
                }
            }
        } catch (final IOException e) {
            // -- LOGGER.error("Failed to get app-status from app-main", e);
            e.printStackTrace();
        }
        return false;
    }

    public static void main(final String[] args) {
        final Client client = new Client();
        System.out.println("Reading batch status ..");
        for (int i = 0; i < 50; i++) {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (final InterruptedException e) {
            }
            final AppStatus st = client.getAppStatus();
            if (st != null) {
                System.out.println(st.toString());
            }
        }

        System.out.println("Request shutdown of server.");
        client.requestShutdown();
    }
}
