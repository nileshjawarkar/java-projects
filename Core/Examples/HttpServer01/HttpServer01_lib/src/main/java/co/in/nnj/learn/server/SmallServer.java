package co.in.nnj.learn.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class SmallServer {
    private final int port;

    public SmallServer(final int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocketChannel server = ServerSocketChannel.open();) {
            server.bind(new InetSocketAddress(port));
            server.configureBlocking(false);
            final Selector selector = Selector.open();
            server.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("Server listeninig on port - " + port + "...");
            while (true) {
                if (selector.select() == 0) {
                    continue;
                }
                try {
                    final Set<SelectionKey> selectedKeys = selector.selectedKeys();
                    final Iterator<SelectionKey> iterator = selectedKeys.iterator();
                    while (iterator.hasNext()) {
                        final SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isAcceptable()) {
                            final SocketChannel channel = server.accept();
                            channel.configureBlocking(false);
                            channel.register(selector, SelectionKey.OP_READ);
                        } else if (key.isReadable()) {
                            handleRequest((SocketChannel) key.channel());
                        }
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private void handleRequest(final SocketChannel channel) throws IOException {
        try (channel;) {
            final ByteBuffer inBuffer = ByteBuffer.allocate(1024);
            final int inDataLen = channel.read(inBuffer);
            if (inDataLen > 0) {
                inBuffer.flip();
                final HttpRequestDetails httpReq = HttpRequestDetails.parse(new String(inBuffer.array(), 0, inDataLen));
                if (httpReq.isValid()) {
                    if (httpReq.url.contains("/srv/metrixs")) {
                        final String metrix = "{\"number\": 10, \"address\": \"any-thing\"}";
                        final String output = "HTTP/1.1 200 OK\r\n\r\n" + metrix + "\n";
                        channel.write(ByteBuffer.wrap(output.getBytes()));
                        return;
                    } else if (httpReq.url.contains("/srv/request_shutdown")) {
                        channel.write(ByteBuffer.wrap("HTTP/1.1 200 OK\r\n\r\nRequested shutdown!\n".getBytes()));
                        return;
                    }
                }
                channel.write(ByteBuffer.wrap("HTTP/1.1 403 Failed\r\n\r\nUrl not found!\n".getBytes()));
            }
        }
    }

    public static void main(final String[] args) {
        final SmallServer server = new SmallServer(5000);
        server.start();
    }
}
