package co.in.nnj.learn.net.ex2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* 1) In this impl, we fixed issues will the simple server that can serve only 1 client at a time.
 * This impl uses executor service threads to manage communication with the clients.
 * 2) But this impl also has its drabacks. Even if we are using threads to manage communication with the
 * clients, system thread are limited. Another problem is that those are in blocked state most of the
 * time and not doding anything. This is just wastting resources and not utiling it properly.
 * 3) In next example, we will see how we can leverage NIO channels to overcome this draback.
 * */
public class MultiThreadedServer {

    public static void main(final String[] args) {
        try (ServerSocket server = new ServerSocket(5000);
                final ExecutorService service = Executors.newCachedThreadPool();) {
            while (true) {
                // -- Blocking call
                final Socket socket = server.accept();

                // -- Using executor service to manage client communication
                service.execute(() -> {
                    manageRequest(socket);
                });
            }
        } catch (final Exception e) {
            System.out.println("Server Error - " + e.getMessage());
        }
    }

    public static void manageRequest(final Socket socket) {
        final String client = socket.getRemoteSocketAddress().toString();
        //-- using try with resource to release socket and streams automatically
        try (socket;) {
            socket.setSoTimeout(30000);
            System.out.println("Connected to client -" + client);
            try (final BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    final PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);) {
                while (true) {
                    final String line = inputStream.readLine();
                    if (line.compareToIgnoreCase("quit") == 0) {
                        break;
                    }
                    System.out.println("Client message - " + line);
                    printWriter.println("Echo from server -" + line);
                }
            }
        } catch (final IOException e) {
            System.out.println("Error for " + client + ", " + e.getMessage());
        } finally {
            System.out.println("Connection closed - " + client);
        }
    }
}
