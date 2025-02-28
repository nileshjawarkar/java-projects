package co.in.nnj.learn.net.ex1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/* This server impl will connect to 1 client and responded to its messages.
 * This is because, after we accept the request, we started responding to it.
 * Until while loop breaks/end, this impl can not reply to the other client and
 * it has to wait till last connection to close.
 */
public class SimpleServer {

    public static void main(final String[] args) {
        try (ServerSocket server = new ServerSocket(5000)) {
            System.out.println("Server listening at port 5000");
            try (Socket socket = server.accept();
                    final BufferedReader inputStream = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    final PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);) {

                System.out.println("Connected to client -" + socket.getRemoteSocketAddress().toString());
                // -- Managing comunication with the client,
                // -- util client send "quit" message.
                while (true) {
                    final String line = inputStream.readLine();
                    if (line.compareToIgnoreCase("quit") == 0) {
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
