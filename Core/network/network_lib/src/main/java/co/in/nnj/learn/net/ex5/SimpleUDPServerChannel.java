package co.in.nnj.learn.net.ex5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SimpleUDPServerChannel {

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

    public static void main(final String[] args) {
        final List<String> quates = readQuates();
        try (DatagramChannel server = DatagramChannel.open()) {
            server.bind(new InetSocketAddress(5000));
            server.configureBlocking(false);
            final Selector selector = Selector.open();
            server.register(selector, SelectionKey.OP_READ);

            final ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (true) {
                selector.select();
                final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    final SelectionKey key = iterator.next();
                    iterator.remove();

                    if (key.isReadable()) {
                        final DatagramChannel channel = (DatagramChannel) key.channel();
                        buffer.clear();
                        final SocketAddress socketAddress = channel.receive(buffer);
                        buffer.flip();

                        final byte[] input = new byte[buffer.remaining()];
                        buffer.get(input);
                        System.out.println("Input => " + new String(input, 0, input.length));

                        final byte[] anyQuate = getAnyQuate(quates).getBytes();
                        int offset = 0;
                        while (offset < anyQuate.length) {
                            buffer.clear();
                            final int length = (anyQuate.length > buffer.remaining() ? buffer.remaining() : anyQuate.length);
                            buffer.put(anyQuate, offset, length);
                            buffer.flip();
                            channel.send(buffer, socketAddress);
                            offset += length;
                        }

                        try {
                            TimeUnit.MILLISECONDS.sleep(5);
                        } catch (final Exception e) {
                        }
                    }
                }
            }
        } catch (final IOException e) {
        }
    }
}
