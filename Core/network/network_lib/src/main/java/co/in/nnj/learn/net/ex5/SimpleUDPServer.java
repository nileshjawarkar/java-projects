package co.in.nnj.learn.net.ex5;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SimpleUDPServer {


    private static void sendQuateToClient(final String quate, final DatagramPacket packet,
            final DatagramSocket server) throws IOException {
        final InetAddress address = packet.getAddress();
        final int port = packet.getPort();

        final byte[] bQuate = quate.getBytes();
        final DatagramPacket resp = new DatagramPacket(bQuate, bQuate.length, address, port);
        server.send(resp);
    }

    public static void main(final String[] args) {
        // -- Read file
        Quates.init();

        try (DatagramSocket server = new DatagramSocket(5000)) {
            // -- Read request -
            while (true) {
                try {
                    final byte[] buf = new byte[1024];
                    final DatagramPacket dgPacket = new DatagramPacket(buf, 1024);
                    server.receive(dgPacket);
                    final String req = new String(buf, 0, dgPacket.getLength());
                    System.out.println("request - " + req);

                    // -- Send respose as quate
                    final String quate = Quates.getAnyQuate();
                    sendQuateToClient(quate, dgPacket, server);
                } catch (final Exception e) {
                }
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
