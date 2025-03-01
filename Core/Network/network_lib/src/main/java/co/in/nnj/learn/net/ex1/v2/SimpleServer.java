package co.in.nnj.learn.net.ex1.v2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/* Uses object stream */
public class SimpleServer {
    public static void main(final String[] args) {
        try (ServerSocket server = new ServerSocket(5000)) {
            System.out.println("Server listenening on port 5000 ...");
            while (true) {
                System.out.println("Server waiting for the request ...");
                try (final Socket socket = server.accept();) {
                    System.out.println("Client connected..");
                    try (final ObjectInputStream sin = new ObjectInputStream(socket.getInputStream());
                            final ObjectOutputStream sout = new ObjectOutputStream(socket.getOutputStream());) {
                        while (true) {
                            try {
                                final Message msg = (Message) sin.readObject();
                                final String strMsg = msg.getData();
                                System.out.println("Input - " + strMsg);
                                if ("quit".equals(strMsg)) {
                                    break;
                                }

                                final Message rmsg = new Message("Your message - " + strMsg);
                                sout.writeObject(rmsg);
                                sout.flush();
                            } catch (final ClassNotFoundException e) {
                                System.out.println("Error1 - " + e.getMessage());
                            }
                        }
                    }
                } finally {
                    System.out.println("Client disconnected");
                }
            }
        } catch (final IOException e) {
            System.out.println("Error2 - " + e.getMessage());
        }
    }
}
