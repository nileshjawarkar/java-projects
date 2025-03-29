package co.in.nnj.learn.server.http;

import java.util.HashMap;
import java.util.Map;

class HttpReqResParser {
    private static int parseHeader(final int start_idx, final String[] content_lines,
            final Map<String, String> headers) {
        int line_idx = start_idx;
        while (line_idx < content_lines.length && content_lines[line_idx].contains(":")) {
            final String[] props = content_lines[line_idx].split(":");
            if (props.length == 2) {
                headers.put(props[0], props[1]);
            }
            line_idx++;
        }
        return line_idx;
    }

    private static String parseData(int line_idx, final String[] content_lines) {
        if (line_idx >= content_lines.length) {
            return null;
        }
        final StringBuffer dataBuffer = new StringBuffer();
        while (line_idx < content_lines.length) {
            dataBuffer.append(content_lines[line_idx]);
            line_idx++;
        }
        return dataBuffer.toString();
    }

    public static HttpRequest parseRequest(final String content) {
        final HttpRequest.Builder builder = HttpRequest.builder();
        if (content != null && content.length() > 0) {
            final String contentt = content.trim();
            final String[] content_lines = contentt.split("\n");
            if (content_lines.length > 0) {
                // -- Part request line - 1st line
                // -- POST /users HTTP/1.1
                final String[] values = content_lines[0].split(" ");
                final int len = values.length;
                if (len > 0) {
                    // -- In this case "POST"
                    builder.withMethod(values[0].trim());
                    if (len == 3) {
                        // -- In this case "/users"
                        builder.withUrlTarget(values[1].trim());
                    }
                }

                int line_idx = 1;
                if (line_idx < content_lines.length) {
                    final Map<String, String> headers = new HashMap<>();
                    line_idx = parseHeader(line_idx, content_lines, headers);
                    builder.withHeaders(headers);
                }

                if (line_idx < content_lines.length) {
                    builder.withData(parseData(line_idx, content_lines));
                }
            }
        }
        return builder.build();
    }

    public static HttpResponse parseResponse(final String content) {
        final HttpResponse.Builder builder = HttpResponse.builder();
        if (content != null && content.length() > 0) {
            final String contentt = content.trim();
            final String[] content_lines = contentt.split("\n");
            if (content_lines.length > 0) {
                // -- HTTP/1.1 200 str_code
                final String[] values = content_lines[0].split(" ");
                final int len = values.length;
                if (len >= 2) {
                    try {
                        builder.withResponseCode(Integer.valueOf(values[1].trim()));
                    } catch (final NumberFormatException e) {
                    }
                }

                int line_idx = 1;
                if (line_idx < content_lines.length) {
                    final Map<String, String> headers = new HashMap<>();
                    line_idx = parseHeader(line_idx, content_lines, headers);
                    builder.withHeaders(headers);
                }

                if (line_idx < content_lines.length) {
                    builder.withData(parseData(line_idx, content_lines));
                }
            }
        }
        return builder.build();
    }
}
