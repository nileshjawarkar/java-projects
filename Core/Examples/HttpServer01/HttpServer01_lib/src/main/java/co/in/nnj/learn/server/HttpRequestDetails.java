package co.in.nnj.learn.server;

public class HttpRequestDetails {
    public final String method;
    public final String url;
    public final String accept;
    public final String content;

    private HttpRequestDetails(final String content, final String method, final String url, final String accept) {
        this.method = method;
        this.url = url;
        this.accept = accept;
        this.content = content;
    }

    @Override
    public String toString() {
        final String m = (method == null ? "" : method);
        return "HttpReqDetails{method=" + m + ", url=" + url + ", accept=" + accept + "}";
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
        String content_v = "";
        String url = "", method = null, accept = "";
        if (content != null && content.length() > 0) {
            content_v = content.trim();
            final String[] arg_lines = content_v.split("\n");
            int line_no = 0;
            while (line_no < arg_lines.length) {
                String line = arg_lines[line_no];
                if (line == null || line.length() == 0) {
                    continue;
                }
                line = line.trim();
                if (line_no == 0) {
                    final String[] values = line.split(" ");
                    final int len = values.length;
                    if (len > 0) {
                        method = values[0].trim();
                        url = null;
                        if (len == 3) {
                            url = values[1].trim();
                        }
                    }
                } else {
                    if (line.contains("Accept") || line.contains("accept")) {
                        final String[] props = line.split(":");
                        if (props.length > 1) {
                            accept = props[1].trim();
                        }
                    }
                }
                line_no++;
            }
        }
        return new HttpRequestDetails(content_v, method, url, accept);
    }
}
