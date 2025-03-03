package co.in.nnj.learn.server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import co.in.nnj.learn.core.AppController;

public class Server implements Runnable {
    private final AppController appController;
    private final int port;

    public Server(final int port, final AppController appController) {
        this.port = port;
        this.appController = appController;
    }

    @Override
    public void run() {
        try (ServerSocketChannel serverChannel = ServerSocketChannel.open()) {
            serverChannel.configureBlocking(false);
            serverChannel.bind(new InetSocketAddress(port));
            System.out.println("Server listening on port [" + port + "]");
            final Selector selector = Selector.open();
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            boolean keepLive = true;
            while (keepLive) {
                if (0 == selector.select()) {
                    continue;
                }
                final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    final SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isAcceptable()) {
                        final ServerSocketChannel srvChannel = (ServerSocketChannel) key.channel();
                        final SocketChannel clientChannel = srvChannel.accept();
                        clientChannel.configureBlocking(false);
                        clientChannel.register(selector, SelectionKey.OP_READ);
                    } else if (key.isReadable()) {
                        final SocketChannel channel = (SocketChannel) key.channel();
                        keepLive = manageComunication(channel);
                    }
                }
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private boolean manageComunication(final SocketChannel channel) {
        // -- Read actual side of input data
        try (channel;) {
            final ByteBuffer inBuf = ByteBuffer.allocate(1024);
            final int numBytes = channel.read(inBuf);
            if (numBytes > 0) {
                inBuf.flip();
                final ByteArrayInputStream baiStream = new ByteArrayInputStream(inBuf.array());
                final ObjectInputStream ooStream = new ObjectInputStream(baiStream);
                final String req = (String) ooStream.readObject();

                // -- final String req = StandardCharsets.UTF_8.decode(inBuf).toString();
                if (ServerRequest.REQ_SHARE_METRIX.equals(req)) {
                    final ByteBuffer respBuf = ByteBuffer.wrap(appController.getAppStatusAsByteArray());
                    channel.write(respBuf);
                } else if (ServerRequest.REQ_M_LIFECYCLE_PRERESTART.equals(req)) {
                    appController.shutdownApp();
                    final ByteBuffer respBuf = ByteBuffer.wrap(ServerRequest.RESP_REQUESTED_SHUTDOWN.getBytes());
                    channel.write(respBuf);
                    return false;
                } else {
                    final ByteBuffer respBuf = ByteBuffer.wrap(ServerRequest.RESP_BAD_REQUEST.getBytes());
                    channel.write(respBuf);
                }
            }
        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                channel.close();
            } catch (final IOException e) {
            }
        }
        return true;
    }
}
