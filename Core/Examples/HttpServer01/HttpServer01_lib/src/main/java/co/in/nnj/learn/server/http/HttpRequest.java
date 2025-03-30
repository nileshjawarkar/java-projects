package co.in.nnj.learn.server.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private final String method;
    private final String urlTarget;
    private final String data;
    private final Map<String, String> headers;
    private final Map<String, String> urlParam;

    private HttpRequest(final Builder builder) {
        this.method = builder.method;
        this.urlTarget = builder.url_target;
        this.data = builder.data;
        this.headers = builder.headers;
        this.urlParam = builder.url_param;
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

    public String getUrlParam(final String param_name) {
        if (urlParam == null) {
            return null;
        }
        return urlParam.get(param_name);
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
        if (urlParam != null) {
            boolean secondItr = false;
            buf.append("?");
            final StringBuffer urlPrmBuf = new StringBuffer();
            for (final String param_name : urlParam.keySet()) {
                if (secondItr)
                    urlPrmBuf.append("&");
                urlPrmBuf.append(param_name);
                urlPrmBuf.append("=");
                urlPrmBuf.append(urlParam.get(param_name));
                secondItr = true;
            }

            final String param_str = urlPrmBuf.toString();
            try {
                buf.append(URLEncoder.encode(param_str, "UTF-8"));
            } catch (final UnsupportedEncodingException e) {
                buf.append(param_str);
            }
        }

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
        private Map<String, String> url_param = null;

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
            final int idxOfQ = url_target.indexOf('?');
            if (idxOfQ != -1) {
                this.url_target = url_target.substring(0, idxOfQ);
                final String args = url_target.substring(idxOfQ + 1);
                // -- System.out.println("url_target = [" + this.url_target + "]");
                // -- System.out.println("args = [" + args + "]");
                if (args != null && args.length() > 0) {
                    this.url_param = new HashMap<>();
                    final String[] arg_lines = args.split("&");
                    if (arg_lines != null && arg_lines.length > 0) {
                        for (final String arg_line : arg_lines) {
                            final String[] keyNValue = arg_line.split("=");
                            if (keyNValue != null && keyNValue.length == 2) {
                                // -- System.out.println("key/value = [" + keyNValue[0] + "/" + keyNValue[1] +
                                // "]");
                                this.url_param.put(keyNValue[0], keyNValue[1]);
                            }
                        }
                    }
                }

            } else {
                this.url_target = url_target;
            }
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
