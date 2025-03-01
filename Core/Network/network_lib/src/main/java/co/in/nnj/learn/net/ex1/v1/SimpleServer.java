package co.in.nnj.learn.net.ex1.v1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/* This example demonstrates use of DataStreams
 */
public class SimpleServer {
    public static void main(final String[] args) {
        try (ServerSocket server = new ServerSocket(5000)) {
            System.out.println("Server listeninig on port 5000 ...");
            while (true) {
                System.out.println("Server waiting for client request ...");
                try (Socket socket = server.accept();) {
                    try (final DataInputStream ins = new DataInputStream(socket.getInputStream());
                            final DataOutputStream outs = new DataOutputStream(socket.getOutputStream());) {
                        while (true) {
                            final int data_size = ins.readInt();
                            if (data_size > 0) {
                                final byte[] data = new byte[data_size];
                                if (ins.read(data) > 0) {
                                    final String in_msg = new String(data);
                                    if (in_msg == null || "".equals(in_msg)) {
                                        continue;
                                    }
                                    System.out.println("Input [" + in_msg + "]");
                                    if ("quit".equals(in_msg)) {
                                        break;
                                    }

                                    final String out_msg = "Your message [" + in_msg + "]";
                                    outs.writeInt(out_msg.length());
                                    outs.write(out_msg.getBytes());
                                    outs.flush();
                                }
                            }
                        }
                    }
                }
            }
        } catch (final IOException e) {
            System.out.println("Server error - " + e.getMessage());
        }
    }
}
