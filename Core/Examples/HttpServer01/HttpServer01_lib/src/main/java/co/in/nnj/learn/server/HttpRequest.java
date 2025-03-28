package co.in.nnj.learn.server;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private final String method;
    private final String urlTarget;
    private final String data;
    private final Map<String, String> headers;

    private HttpRequest(final Builder builder) {
        this.method = builder.method;
        this.urlTarget = builder.url_target;
        this.data = builder.data;
        this.headers = builder.headers;
    }

    public String getMethod() {
        return method;
    }

    public String getUrlTarget() {
        return urlTarget;
    }

    public String getData() {
        return data;
    }

    public String getHeaders(final String header) {
        if (headers == null)
            return null;
        return headers.get(header);
    }

    @Override
    public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append(method);
        buf.append(" ");
        buf.append(urlTarget);
        buf.append(" HTTP/1.1\r\n");
        if (headers != null) {
            for (final String key : headers.keySet()) {
                buf.append(key);
                buf.append(": ");
                buf.append(headers.get(key));
                buf.append("\r\n");
            }
        }
        if (data != null && method.compareToIgnoreCase("GET") != 0) {
            buf.append("\r\n");
            buf.append(data);
            buf.append("\n");
        }
        return buf.toString();
    }

    public boolean isValid() {
        final boolean hasMethod = (method != null &&
                (method.compareToIgnoreCase("GET") == 0 ||
                        method.compareToIgnoreCase("POST") == 0 ||
                        method.compareToIgnoreCase("PUT") == 0 ||
                        method.compareToIgnoreCase("DELETE") == 0));
        return hasMethod;
    }

    public static class Builder {
        private Map<String, String> headers = null;
        private String method = "GET";
        private String url_target = "/";
        private String data = null;

        public Builder withHeaders(final Map<String, String> headers) {
            if (headers == null)
                this.headers = headers;
            return this;
        }

        public Builder withHeader(final String key, final String value) {
            if (headers == null)
                headers = new HashMap<>();
            return this;
        }

        public Builder withMethod(final String method) {
            this.method = method;
            return this;
        }

        public Builder withUrlTarget(final String url_target) {
            this.url_target = url_target;
            return this;
        }

        public Builder withData(final String data) {
            this.data = data;
            return this;
        }

        public HttpRequest build() {
            return new HttpRequest(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static HttpRequest fromString(final String content) {
        return HttpReqResParser.parseRequest(content);
    }
}
