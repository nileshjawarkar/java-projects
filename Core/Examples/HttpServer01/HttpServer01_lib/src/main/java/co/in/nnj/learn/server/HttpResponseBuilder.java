package co.in.nnj.learn.server;

import java.util.HashMap;
import java.util.Map;

public class HttpResponseBuilder {

    public static enum Status {
        OK(200), CREATED(201), ACCEPTED(202),
        NO_CONTENT(204), REDIRECT(302), BAD_REQUEST(400),
        UNAUTHORIZED(401), FORBIDDEN(403), NOT_FOUND(404),
        REQUEST_TIMEOUT(408), UNSUPORTED_MEDIA_TYTPE(415), INTERNAL_SERVER_ERROR(500),
        NOT_IMPlEMENTED(501);

        private final int code;

        Status(final int code) {
            this.code = code;
        }

        @Override
        public String toString() {
            switch (code) {
                case 200:
                    return "Ok";
                case 201:
                    return "Created";
                case 202:
                    return "Accepted";
                case 204:
                    return "No content";
                case 302:
                    return "Redirect";
                case 400:
                    return "Bad request";
                case 401:
                    return "Unauthorized";
                case 403:
                    return "Forbidden";
                case 404:
                    return "Not found";
                case 408:
                    return "Request Timeout";
                case 415:
                    return "Unsupported Media Type";
                case 500:
                    return "Internal Server Error";
                case 501:
                    return "Not Implemented";
                case 503:
                    return "Service Unavailable";
                default:
                    return "Not Supported";
            }
        }

        public int intValue() {
            return code;
        }
    }

    private Status status_code;
    private String content = null;
    private final Map<String, String> headers = new HashMap<>();

    public HttpResponseBuilder withResponseCode(final Status code) {
        this.status_code = code;
        return this;
    }

    public HttpResponseBuilder withResponse(final String content) {
        this.content = content;
        return this;
    }

    public HttpResponseBuilder withHeader(final String key, final String value) {
        headers.put(key, value);
        return this;
    }

    public String build() {
        final StringBuffer buf = new StringBuffer();
        buf.append("HTTP/1.1 ");
        buf.append(status_code.intValue());
        buf.append(" ");
        buf.append(status_code.toString());
        buf.append("\r\n");
        for (final String key : headers.keySet()) {
            buf.append(key);
            buf.append(": ");
            buf.append(headers.get(key));
            buf.append("\r\n");
        }
        if (content != null) {
            buf.append("\r\n");
            buf.append(content);
            buf.append("\n");
        }
        return buf.toString();
    }
}
