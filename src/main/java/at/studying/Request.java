package at.studying;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Request {

    private Map<String, String> headers = new HashMap<>();

    // GET, POST, PUT, DELETE
    private String method;

    private String route;

    private String host;

    // application/json, text/plain
    private String contentType;

    // 0, 17
    private int contentLength;

    private String body;

    public String getMethod() {
        return method;
    }

    public void setMethod(HttpMethod httpMethod) {
        this.method = httpMethod.getMethod();
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getHeader(String name) {
        return headers.get(name.toLowerCase());
    }

    public String getQueryParam(String key) {
        if (this.route.contains("?")) {
            String queryString = this.route.substring(this.route.indexOf("?") + 1);
            Map<String, String> queryParameters = Arrays.stream(queryString.split("&"))
                    .map(param -> param.split("="))
                    .collect(Collectors.toMap(
                            p -> p[0],
                            p -> p.length > 1 ? p[1] : ""
                    ));
            return queryParameters.get(key);
        }
        return null;
    }
    public void addHeader(String name, String value) {
        this.headers.put(name.toLowerCase(), value);
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }
}
