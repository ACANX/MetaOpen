package com.acanx.meta.base.http.c;

/**
 * Common HTTP constants.
 */
public final class BaseHttpConst {

    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_PUT = "PUT";
    public static final String METHOD_PATCH = "PATCH";
    public static final String METHOD_DELETE = "DELETE";
    public static final String METHOD_HEAD = "HEAD";
    public static final String METHOD_OPTIONS = "OPTIONS";
    public static final String METHOD_TRACE = "TRACE";
    public static final String METHOD_CONNECT = "CONNECT";

    public static final String HEADER_ACCEPT = "Accept";
    public static final String HEADER_ACCEPT_CHARSET = "Accept-Charset";
    public static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    public static final String HEADER_ACCEPT_LANGUAGE = "Accept-Language";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String HEADER_CACHE_CONTROL = "Cache-Control";
    public static final String HEADER_CONNECTION = "Connection";
    public static final String HEADER_CONTENT_ENCODING = "Content-Encoding";
    public static final String HEADER_CONTENT_LENGTH = "Content-Length";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_COOKIE = "Cookie";
    public static final String HEADER_HOST = "Host";
    public static final String HEADER_IF_MATCH = "If-Match";
    public static final String HEADER_IF_NONE_MATCH = "If-None-Match";
    public static final String HEADER_LOCATION = "Location";
    public static final String HEADER_ORIGIN = "Origin";
    public static final String HEADER_REFERER = "Referer";
    public static final String HEADER_RETRY_AFTER = "Retry-After";
    public static final String HEADER_SET_COOKIE = "Set-Cookie";
    public static final String HEADER_USER_AGENT = "User-Agent";
    public static final String HEADER_WWW_AUTHENTICATE = "WWW-Authenticate";
    public static final String HEADER_X_REQUEST_ID = "X-Request-Id";
    public static final String DEFAULT_UA = "MetaOpen/0.8.3";
    public static final String AUTH_SCHEME_BEARER = "Bearer";

    public static final String MEDIA_TYPE_APPLICATION_JSON = "application/json";
    public static final String MEDIA_TYPE_APPLICATION_XML = "application/xml";
    public static final String MEDIA_TYPE_APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded";
    public static final String MEDIA_TYPE_APPLICATION_OCTET_STREAM = "application/octet-stream";
    public static final String MEDIA_TYPE_APPLICATION_PDF = "application/pdf";
    public static final String MEDIA_TYPE_APPLICATION_PROBLEM_JSON = "application/problem+json";
    public static final String MEDIA_TYPE_APPLICATION_NDJSON = "application/x-ndjson";
    public static final String MEDIA_TYPE_MULTIPART_FORM_DATA = "multipart/form-data";
    public static final String MEDIA_TYPE_TEXT_PLAIN = "text/plain";
    public static final String MEDIA_TYPE_TEXT_HTML = "text/html";
    public static final String MEDIA_TYPE_TEXT_CSS = "text/css";
    public static final String MEDIA_TYPE_TEXT_EVENT_STREAM = "text/event-stream";
    public static final String MEDIA_TYPE_APPLICATION_JSON_UTF8 = MEDIA_TYPE_APPLICATION_JSON + "; charset=UTF-8";
    public static final String MEDIA_TYPE_TEXT_PLAIN_UTF8 = MEDIA_TYPE_TEXT_PLAIN + "; charset=UTF-8";
    public static final String MEDIA_TYPE_TEXT_HTML_UTF8 = MEDIA_TYPE_TEXT_HTML + "; charset=UTF-8";

    public static final String SCHEME_HTTP = "http";
    public static final String SCHEME_HTTPS = "https";
    public static final String SCHEME_WS = "ws";
    public static final String SCHEME_WSS = "wss";

    public static final int STATUS_CONTINUE = 100;
    public static final int STATUS_SWITCHING_PROTOCOLS = 101;
    public static final int STATUS_OK = 200;
    public static final int STATUS_CREATED = 201;
    public static final int STATUS_ACCEPTED = 202;
    public static final int STATUS_NO_CONTENT = 204;
    public static final int STATUS_MULTIPLE_CHOICES = 300;
    public static final int STATUS_MOVED_PERMANENTLY = 301;
    public static final int STATUS_FOUND = 302;
    public static final int STATUS_NOT_MODIFIED = 304;
    public static final int STATUS_BAD_REQUEST = 400;
    public static final int STATUS_UNAUTHORIZED = 401;
    public static final int STATUS_FORBIDDEN = 403;
    public static final int STATUS_NOT_FOUND = 404;
    public static final int STATUS_METHOD_NOT_ALLOWED = 405;
    public static final int STATUS_CONFLICT = 409;
    public static final int STATUS_UNPROCESSABLE_ENTITY = 422;
    public static final int STATUS_TOO_MANY_REQUESTS = 429;
    public static final int STATUS_INTERNAL_SERVER_ERROR = 500;
    public static final int STATUS_BAD_GATEWAY = 502;
    public static final int STATUS_SERVICE_UNAVAILABLE = 503;
    public static final int STATUS_GATEWAY_TIMEOUT = 504;

    private BaseHttpConst() {
    }
}
