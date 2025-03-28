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

public class HttpControlServer {
    private final int port;

    public HttpControlServer(final int port) {
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
                final HttpRequest httpReq = HttpRequest.fromString(new String(inBuffer.array(), 0, inDataLen));
                final HttpResponse.Builder respBuilder = HttpResponse.builder();
                if (httpReq.isValid()) {
                    final String target = httpReq.getUrlTarget();
                    if (target.startsWith("/srv/metrixs")) {
                        final String metrix = "{\"number\": 10, \"address\": \"any-thing\"}";

                        final HttpResponse resp = respBuilder.withResponseCode(HttpResponseCode.OK)
                                .withHeader("Content-Type", "application/json")
                                .withData(metrix).build();
                        channel.write(ByteBuffer.wrap(resp.toBytes()));
                        return;
                    } else if (target.startsWith("/srv/request_shutdown")) {
                        final HttpResponse resp = respBuilder.withResponseCode(HttpResponseCode.OK)
                                .withData("Requested shutdown!").build();
                        channel.write(ByteBuffer.wrap(resp.toBytes()));
                        return;
                    }
                }
                final HttpResponse rep = respBuilder.withResponseCode(HttpResponseCode.BAD_REQUEST)
                        .withData("").build();
                channel.write(ByteBuffer.wrap(rep.toBytes()));
            }
        }
    }

    public static void main(final String[] args) {
        final HttpControlServer server = new HttpControlServer(5000);
        server.start();
    }
}
