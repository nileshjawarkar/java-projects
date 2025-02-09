package co.in.nnj.learn.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SimpleClient {

    public static void main(final String[] args) {
        try (Socket socket = new Socket("localhost", 5000)) {
            final BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            final PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            String input;
            final Scanner scanner = new Scanner(System.in);
            System.out.println("Enter some message for server - ");
            do {
                input = scanner.nextLine();
                printWriter.println(input);

                if (input.compareToIgnoreCase("quit") != 0) {
                    System.out.println(inputStream.readLine());
                }
            } while (input.compareToIgnoreCase("quit") != 0);
            scanner.close();
        } catch (final IOException e) {
            System.out.println(e.getMessage());
        } finally {
            System.out.println("Client finally disconneted");
        }
    }
}
