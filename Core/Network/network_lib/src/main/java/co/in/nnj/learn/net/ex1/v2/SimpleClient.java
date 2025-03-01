package co.in.nnj.learn.net.ex1.v2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class SimpleClient {
    public static void main(final String[] args) {
        try (final Socket socket = new Socket("localhost", 5000)) {
            final Scanner scanner = new Scanner(System.in);
            System.out.println("Connected to server ..");
            try (final ObjectOutputStream sout = new ObjectOutputStream(socket.getOutputStream());
                    final ObjectInputStream sin = new ObjectInputStream(socket.getInputStream());) {
                while (true) {
                    try {
                        System.out.print("Your message: ");
                        final String strMsg = scanner.nextLine();
                        final Message msg = new Message(strMsg);
                        sout.writeObject(msg);
                        sout.flush();
                        if ("quit".equals(strMsg)) {
                            break;
                        }

                        final Message respMsg = (Message) sin.readObject();
                        System.out.println(respMsg);
                    } catch (final ClassNotFoundException e) {
                        System.out.println("Error Message - " + e.getMessage());
                    }
                }
                scanner.close();
            }
        } catch (final IOException e) {
            System.out.println("Error Message - " + e.getMessage());
        }
    }
}
