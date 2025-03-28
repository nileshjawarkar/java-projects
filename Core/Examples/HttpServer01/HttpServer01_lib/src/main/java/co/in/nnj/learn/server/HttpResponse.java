package co.in.nnj.learn.server;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private final Map<String, String> headers;
    private final String data;
    private final HttpResponseCode status_code;

    public HttpResponse(final Builder builder) {
        this.headers = builder.headers;
        this.data = builder.data;
        this.status_code = builder.status_code;
    }

    public String getHeaders(final String key) {
        return headers.get(key);
    }

    public String getData() {
        return data;
    }

    public HttpResponseCode getStatusCode() {
        return status_code;
    }

    @Override
    public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append("HTTP/1.1 ");
        buf.append(status_code.intValue());
        buf.append(" ");
        buf.append(status_code.toString());
        buf.append("\r\n");
        if (headers != null) {
            for (final String key : headers.keySet()) {
                buf.append(key);
                buf.append(": ");
                buf.append(headers.get(key));
                buf.append("\r\n");
            }
        }
        if (data != null) {
            buf.append("\r\n");
            buf.append(data);
            buf.append("\n");
        }
        return buf.toString();
    }

    public static class Builder {
        private HttpResponseCode status_code = HttpResponseCode.BAD_REQUEST;
        private String data = null;
        private Map<String, String> headers = null;

        public Builder withResponseCode(final int code) {
            status_code = HttpResponseCode.fromInt(code);
            return this;
        }

        public Builder withResponseCode(final HttpResponseCode code) {
            status_code = code;
            return this;
        }

        public Builder withHeaders(final Map<String, String> headers) {
            if (headers == null)
                this.headers = headers;
            return this;
        }

        public Builder withHeader(final String key, final String value) {
            if (headers == null)
                this.headers = new HashMap<>();
            headers.put(key, value);
            return this;
        }

        public Builder withData(final String data) {
            this.data = data;
            return this;
        }

        public HttpResponse build() {
            return new HttpResponse(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static HttpResponse fromString(final String content) {
        return HttpReqResParser.parseResponse(content);
    }

    public byte[] toBytes() {
        return toString().getBytes();
    }
}
