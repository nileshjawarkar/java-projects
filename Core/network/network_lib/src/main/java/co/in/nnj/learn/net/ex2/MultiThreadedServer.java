package co.in.nnj.learn.net.ex2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadedServer {

    public static void main(final String[] args) {
        try (ServerSocket server = new ServerSocket(5000);
                final ExecutorService service = Executors.newCachedThreadPool();) {
            while (true) {
                final Socket socket = server.accept();
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
        try (socket;) {
            socket.setSoTimeout(30000);
            System.out.println("Connected to client -" + client);
            final BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            final PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);

            while (true) {
                final String line = inputStream.readLine();
                if (line.compareToIgnoreCase("quit") == 0) {
                    break;
                }
                System.out.println("Client message - " + line);
                printWriter.println("Echo from server -" + line);
            }
        } catch (final IOException e) {
            System.out.println("Error for " + client + ", " + e.getMessage());
        } finally {
            System.out.println("Connection closed - " + client);
        }
    }
}
