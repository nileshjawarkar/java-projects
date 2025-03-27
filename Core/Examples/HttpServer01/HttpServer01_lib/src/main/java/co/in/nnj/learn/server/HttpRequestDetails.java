package co.in.nnj.learn.server;

import java.util.HashMap;
import java.util.Map;

public class HttpRequestDetails {
    private final String method;
    private final String urlTarget;
    private final String body;
    private final Map<String, String> headers;

    private HttpRequestDetails(final String method, final String url_target, final String body, final Map<String, String> headers) {
        this.method = method;
        this.urlTarget = url_target;
        this.body = body;
        this.headers = headers;
    }

    public String getMethod() {
        return method;
    }
    public String getUrlTarget() {
        return urlTarget;
    }
    public String getBody() {
        return body;
    }
    public String getHeaders(final String key) {
        return headers.get(key);
    }

    @Override
    public String toString() {
        final String m = (method == null ? "" : method);
        return "HttpReq {method=" + m + ", url=" + urlTarget + "}";
    }

    public boolean isValid() {
        final boolean hasMethod = (method != null &&
                (method.compareToIgnoreCase("GET") == 0 ||
                        method.compareToIgnoreCase("POST") == 0 ||
                        method.compareToIgnoreCase("PUT") == 0 ||
                        method.compareToIgnoreCase("DELETE") == 0));
        return hasMethod;
    }

    public static HttpRequestDetails parse(final String content) {
        String contentt = "", body = "";
        String url = "", method = null;
        final Map<String, String> header = new HashMap<>();
        if (content != null && content.length() > 0) {
            contentt = content.trim();
            final String[] arg_lines = contentt.split("\n");
            if (arg_lines.length > 0) {
                // -- Part request line - 1st line
                // -- POST /users HTTP/1.1
                final String[] values = arg_lines[0].split(" ");
                final int len = values.length;
                if (len > 0) {
                    // -- In this case "POST"
                    method = values[0].trim();
                    url = null;
                    if (len == 3) {
                        // -- In this case "/users"
                        url = values[1].trim();
                    }
                }

                // -- Parse header seperated by ":"
                int line_no = 1;
                while (line_no < arg_lines.length && arg_lines[line_no].contains(":")) {
                    final String[] props = arg_lines[line_no].split(":");
                    if(props.length == 2) {
                        header.put(props[0], props[1]);
                    }
                    line_no++;
                }

                // -- Now it all http request body
                if (line_no < arg_lines.length) {
                    final StringBuffer buf = new StringBuffer();
                    while (line_no < arg_lines.length) {
                        buf.append(arg_lines[line_no]);
                        line_no++;
                    }
                    body = buf.toString();
                }
            }

        }
        return new HttpRequestDetails(method, url, body, header);
    }
}
