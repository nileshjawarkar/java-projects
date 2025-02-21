package co.in.nnj.learn.net.ex5;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/*
 * receive and send
 */
public class SimpleUDPServerWithSelector {

    public static void main(final String[] args) {
        Quates.init();
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

                        final byte[] anyQuate = Quates.getAnyQuate().getBytes();
                        int offset = 0;
                        while (offset < anyQuate.length) {
                            buffer.clear();
                            final int buf_len = buffer.remaining();
                            final int length = (anyQuate.length > buf_len ? buf_len : anyQuate.length);
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
