package co.in.nnj.learn.server.http;

public enum HttpResponseCode {
    OK(200), CREATED(201), ACCEPTED(202),
    NO_CONTENT(204), REDIRECT(302), BAD_REQUEST(400),
    UNAUTHORIZED(401), FORBIDDEN(403), NOT_FOUND(404),
    REQUEST_TIMEOUT(408), UNSUPORTED_MEDIA_TYTPE(415), INTERNAL_SERVER_ERROR(500),
    NOT_IMPlEMENTED(501), SERVICE_UNAVAILABLE(503);

    private final int code;

    HttpResponseCode(final int code) {
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

    public static HttpResponseCode fromInt(final int code) {
        switch (code) {
            case 200:
                return HttpResponseCode.OK;
            case 201:
                return HttpResponseCode.CREATED;
            case 202:
                return HttpResponseCode.ACCEPTED;
            case 204:
                return HttpResponseCode.NO_CONTENT;
            case 302:
                return HttpResponseCode.REDIRECT;
            case 400:
                return HttpResponseCode.BAD_REQUEST;
            case 401:
                return HttpResponseCode.UNAUTHORIZED;
            case 403:
                return HttpResponseCode.FORBIDDEN;
            case 404:
                return HttpResponseCode.NOT_FOUND;
            case 408:
                return HttpResponseCode.REQUEST_TIMEOUT;
            case 415:
                return HttpResponseCode.UNSUPORTED_MEDIA_TYTPE;
            case 500:
                return HttpResponseCode.INTERNAL_SERVER_ERROR;
            case 501:
                return HttpResponseCode.NOT_IMPlEMENTED;
            case 503:
                return HttpResponseCode.SERVICE_UNAVAILABLE;
            default:
                return HttpResponseCode.NOT_IMPlEMENTED;
        }
    }
}
