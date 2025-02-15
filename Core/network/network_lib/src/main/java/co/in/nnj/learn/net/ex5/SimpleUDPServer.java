package co.in.nnj.learn.net.ex5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class SimpleUDPServer {

    private static List<String> readQuates() {
        final ArrayList<String> quates = new ArrayList<>();
        final InputStream inputStream = SimpleUDPServer.class.getResourceAsStream("quotes.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line = reader.readLine();
            while (line != null) {
                quates.add(line);
                line = reader.readLine();
            }
        } catch (final IOException e) {
        }
        // -- Start udp server
        System.out.println("Number of quates available - " + quates.size());
        return quates;
    }

    static String getAnyQuate(final List<String> quates) {
        final int index = (int) (Math.random() * quates.size());
        if (index < quates.size()) {
            return quates.get(index);
        }
        return "Patience is bitter, but its fruit is sweet. Try one more time.";
    }

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
        final List<String> quates = readQuates();

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
                    final String quate = getAnyQuate(quates);
                    sendQuateToClient(quate, dgPacket, server);
                } catch (final Exception e) {
                }
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
