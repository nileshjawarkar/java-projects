package co.in.nnj.learn.net.ex4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class SimpleServerWithSelectors {

    public static void main(final String[] args) {
        try (ServerSocketChannel serverChannel = ServerSocketChannel.open()) {
            serverChannel.bind(new InetSocketAddress(5000));
            System.out.println("Server listening on - " + serverChannel.getLocalAddress());
            serverChannel.configureBlocking(false);

            //-- Create a selector and register it sever channel.
            //-- It is registered for OP_ACCEPT, it means (i think), when server will get 
            //-- request for connection from client, it will generated event.
            final Selector selector = Selector.open();
            //-- Registration 1
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            //-- Listen for events
            while (true) {
                //-- Following line will block the execution, until 
                //-- server will get connection request from client.
                selector.select();

                //-- If execution is here, it mean we have some selection event generated.
                //-- Either it is for OP_ACCEPT, OR it is for OP_READ
                //-- These are the 2 events, for which current impl registered for.
                final Set<SelectionKey> selectedKeys = selector.selectedKeys();
                final Iterator<SelectionKey> iterator = selectedKeys.iterator();

                while (iterator.hasNext()) {
                    final SelectionKey key = iterator.next();
                    //-- Remove event from list. **MUST
                    iterator.remove();
                    if (key.isAcceptable()) {
                        //-- Accept the request and configure the channel for NB-IO
                        final SocketChannel channel = serverChannel.accept();
                        System.out.println("Connected to -" + channel.getRemoteAddress());
                        channel.configureBlocking(false);

                        //-- Register for READ type event.
                        //-- This will be trigged when user send some message
                        channel.register(selector, SelectionKey.OP_READ);
                    } else if (key.isReadable()) {
                        manageComunication(key);
                    }
                }
            }
        } catch (final IOException e) {
            System.out.println("Server Error - " + e.getMessage());
        }
    }

    private static void manageComunication(final SelectionKey key) {
        final SocketChannel channel = (SocketChannel) key.channel();
        try {
            final ByteBuffer buffer = ByteBuffer.allocate(1024);
            // -- Read input from client
            final int readBytes = channel.read(buffer);

            // -- if readBytes > 0, we have input
            // -- if readBytes == -1, connection is closed.
            if (readBytes > 0) {
                // -- Flip the buffer to prepare it for reading.
                // -- Example Buffer[pos=200, limit=1000, capacity=2000], flip will change it to
                // -- Buffer[pos=0, limit=200, capacity=2000]
                // -- Now we can read for pos=0 to 200
                buffer.flip();

                // -- Send response to client along with the message he send to the server
                channel.write(ByteBuffer.wrap("Echo from server: ".getBytes()));
                while (buffer.hasRemaining()) {
                    channel.write(buffer);
                }
                buffer.clear();
            } else if (readBytes == -1) {
                System.out.printf("Connection to %s lost%n",
                        channel.socket().getRemoteSocketAddress());
                // -- Close the channel and remove it entry from saved
                // -- list
                channel.close();
            }
        } catch (final Exception e) {
            System.out.println("Server Error - " + e.getMessage());
        }
    }
}
