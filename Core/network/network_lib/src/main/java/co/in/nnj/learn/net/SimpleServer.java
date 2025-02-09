package co.in.nnj.learn.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer {

    public static void main(final String[] args) {
        try (ServerSocket server = new ServerSocket(5000)) {
            try (Socket socket = server.accept()) {
                System.out.println("Connected to client -");
                final BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                final PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);

                while (true) {
                    final String line = inputStream.readLine();
                    if(line.compareToIgnoreCase("quit") == 0) {
                        break;
                    }
                    System.out.println("Client message - " + line);
                    printWriter.println("Echo from server -" + line);
                }
            }
        } catch (final Exception e) {
            System.out.println("Server Error - " + e.getMessage());
        }
    }
}
