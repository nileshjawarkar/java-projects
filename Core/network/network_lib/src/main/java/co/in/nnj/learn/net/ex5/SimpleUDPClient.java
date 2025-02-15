package co.in.nnj.learn.net.ex5;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SimpleUDPClient {
    public static void main(final String[] args) {
        try (DatagramSocket client = new DatagramSocket()) {
            final String req = "Please share some good quate.";
            final byte[] bReq = req.getBytes();
            final DatagramPacket packet = new DatagramPacket(bReq, bReq.length, InetAddress.getLocalHost(), 5000);
            client.send(packet);

            final byte[] buf = new byte[2048];
            final DatagramPacket resp = new DatagramPacket(buf, 2048);
            client.receive(resp);

            final String resp_str = new String(buf, 0, resp.getLength());
            System.out.println("\n" + resp_str + "\n");
        } catch (final IOException e) {
        }
    }
}
