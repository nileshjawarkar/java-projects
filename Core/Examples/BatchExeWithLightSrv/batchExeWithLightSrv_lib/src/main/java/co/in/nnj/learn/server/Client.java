package co.in.nnj.learn.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.in.nnj.learn.core.AppStatus;

public class Client {
    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class.getName());
    public AppStatus getAppStatus() {
        try (final Socket socket = new Socket("localhost", 5000)) {
            try (final ObjectOutputStream sout = new ObjectOutputStream(socket.getOutputStream());
                    final ObjectInputStream sin = new ObjectInputStream(socket.getInputStream());) {
                sout.writeObject(ServerRequest.REQ_SHARE_METRIX);
                sout.flush();
                final AppStatus appStatus = (AppStatus) sin.readObject();
                return appStatus;
            }
        } catch (final IOException | ClassNotFoundException e) {
           LOGGER.error("Failed to get app-status from app-main", e); 
        }
        return null;
    }

    public boolean requestShutdown() {
        return false;
    }
}
