package co.in.nnj.learn.net.ex1.v1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class SimpleClient {

    public static void main(final String[] args) {
        try (Socket socket = new Socket("localhost", 5000)) {
            try (DataOutputStream sout = new DataOutputStream(socket.getOutputStream());
                    DataInputStream sint = new DataInputStream(socket.getInputStream());
                    final Scanner scanner = new Scanner(System.in);) {
                while (true) {
                    // -- send message.
                    // -- get message for user.
                    System.out.print("Your massage: ");
                    final String msg = scanner.nextLine();
                    if (msg == null || "".equals(msg)) {
                        continue;
                    }

                    sout.writeInt(msg.length());
                    sout.write(msg.getBytes());
                    sout.flush();

                    if("quit".equals(msg)) {
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (final InterruptedException e) {
                        }
                        break;
                    }

                    // -- Read message
                    final int msg_size = sint.readInt();
                    if (msg_size > 0) {
                        final byte[] data = new byte[msg_size];
                        if (sint.read(data) > 0) {
                            final String res = new String(data);
                            System.out.println("Response " + res);
                        }
                    }
                }
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
