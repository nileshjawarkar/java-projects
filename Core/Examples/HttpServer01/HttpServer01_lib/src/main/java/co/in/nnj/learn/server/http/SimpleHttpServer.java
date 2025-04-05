package co.in.nnj.learn.server.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class SimpleHttpServer implements Runnable {

    @FunctionalInterface
    public static interface HttpRouter {
        void service(HttpRequest req, HttpResponse.Builder responseBuilder);
    }

    private final int port;
    private final Map<String, HttpRouter> routes;

    private SimpleHttpServer(final Builder builder) {
        this.port = builder.port;
        this.routes = builder.routes;
    }

    private void handleRequest(final SocketChannel channel) throws IOException {
        try (channel;) {
            final ByteBuffer inBuffer = ByteBuffer.allocate(1024);
            final int inDataLen = channel.read(inBuffer);
            if (inDataLen > 0) {
                inBuffer.flip();
                final HttpRequest httpReq = HttpRequest.fromString(new String(inBuffer.array(), 0, inDataLen));
                final HttpResponse.Builder respBuilder = HttpResponse.builder(channel);
                if (httpReq.isValid()) {
                    final String target = httpReq.getUrlTarget();
                    if (routes.containsKey(target)) {
                        final HttpRouter router = routes.get(target);
                        router.service(httpReq, respBuilder);
                        return;
                    }
                }
                final HttpResponse rep = respBuilder.withResponseCode(HttpResponseCode.BAD_REQUEST)
                        .withData("Route not found. Please check your inputs.").build();
                channel.write(ByteBuffer.wrap(rep.toBytes()));
            }
        }
    }

    public static class Builder {
        private int port = -1;
        private final Map<String, HttpRouter> routes = new HashMap<>();

        public Builder withPort(final int port) {
            this.port = port;
            return this;
        }

        public Builder withRoute(final String path, final HttpRouter route) {
            routes.put(path, route);
            return this;
        }

        public SimpleHttpServer build() {
            if (port < 1 || routes.size() == 0) {
                throw new RuntimeException("Missing mandatory arguments - port or routes.");
            }
            return new SimpleHttpServer(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private boolean keepRunning = true;

    @Override
    public void run() {
        try (ServerSocketChannel server = ServerSocketChannel.open();) {
            server.bind(new InetSocketAddress(port));
            server.configureBlocking(false);
            final Selector selector = Selector.open();
            server.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("Server listeninig on port - " + port + "...");
            while (keepRunning) {
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

    public void stop() {
        keepRunning = false;
    }
}
