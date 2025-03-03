package co.in.nnj.learn.server;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Time;
import java.util.concurrent.TimeUnit;

import co.in.nnj.learn.core.AppStatus;

public class Client {
    // -- private static final Logger LOGGER =
    // LoggerFactory.getLogger(Client.class.getName());

    public AppStatus getAppStatus() {
        try (final Socket socket = new Socket("localhost", 5000)) {
            try (final OutputStream outputStream = socket.getOutputStream();) {

                // -- Connvert object to byte array
                final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                final ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(ServerRequest.REQ_SHARE_METRIX);
                oos.flush();

                // -- Write to socket
                outputStream.write(baos.toByteArray());
                outputStream.flush();

                try (final ObjectInputStream sin = new ObjectInputStream(socket.getInputStream());) {
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
                final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                final ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(ServerRequest.REQ_M_LIFECYCLE_PRERESTART);
                oos.flush();

                // -- Write to socket
                outputStream.write(baos.toByteArray());
                outputStream.flush();

                try (final BufferedReader reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));) {
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
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (final InterruptedException e) {
        }

        final Client client = new Client();
        final AppStatus st = client.getAppStatus();
        if(st != null) {
            System.out.println(st.toString());
        }

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (final InterruptedException e) {
        }

        client.requestShutdown();
    }
}
