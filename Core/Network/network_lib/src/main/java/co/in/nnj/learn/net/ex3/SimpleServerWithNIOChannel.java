package co.in.nnj.learn.net.ex3;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

public class SimpleServerWithNIOChannel {

    public static void main(final String[] args) {
        try (ServerSocketChannel serverChannel = ServerSocketChannel.open()) {
            // -- Setup server to listen on port 5000 and configure it for NON-BLOCKING IO
            serverChannel.bind(new InetSocketAddress(5000));
            serverChannel.configureBlocking(false);
            System.out.println("Server listning on port - " + serverChannel.socket().getLocalPort());

            // -- Manage client request here
            final List<SocketChannel> clientChannels = new ArrayList<>();
            while (true) {
                final SocketChannel clientChannel = serverChannel.accept();
                if (clientChannel != null) {
                    clientChannel.configureBlocking(false);
                    clientChannels.add(clientChannel);
                    System.out.println("Connected to client - " + clientChannel.getRemoteAddress());
                }

                final ByteBuffer buffer = ByteBuffer.allocate(1024);
                for (int i = 0; i < clientChannels.size(); i++) {
                    try {
                        final SocketChannel channel = clientChannels.get(i);
                        // -- Read input from client
                        final int readBytes = channel.read(buffer);
                        // -- If user didnt send any thing, then read will get blocked.
                        // -- if read returns -1, then connection is closed.
                        // -- if read returns value > 0, then user send some data.
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
                            clientChannels.remove(i);
                        }
                    } catch (final Exception e) {
                        System.out.println("Server/client Error - " + e.getMessage());
                    }
                }
            }

        } catch (final IOException e) {
            System.out.println("Server Error - " + e.getMessage());
        }
    }
}
