package co.in.nnj.learn.net.ex1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/* This client impl will connect to server at localhost:5000.
 * Then client can send messages to server and wait for its response,
 * and print response on the screen.
 * If client send "quit" message to server, this will end connection with the server.
 */
public class SimpleClient {

    public static void main(final String[] args) {
        //-- connect to server at 5000
        try (Socket socket = new Socket("localhost", 5000)) {
            //-- Setup input, output streams
            final BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            final PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);

            //-- setup input reading 
            String input;
            final Scanner scanner = new Scanner(System.in);
            System.out.println("Send message to server : ");

            //-- Start the loop
            do {
                //-- Read input message provided by user
                System.out.print("Enter message - ");
                input = scanner.nextLine();
                //-- Send it to servr
                printWriter.println(input);

                //-- Wait for the reply and print it
                //-- If quit message send, client will not wait, 
                //-- it will break the loop
                if (input.compareToIgnoreCase("quit") != 0) {
                    System.out.println(inputStream.readLine());
                }
            } while (input.compareToIgnoreCase("quit") != 0);
            scanner.close();
        } catch (final IOException e) {
            System.out.println(e.getMessage());
        } finally {
            System.out.println("Client finally disconnected");
        }
    }
}
