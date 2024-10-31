package at.studying;

import at.studying.HttpMethod;
import at.studying.Request;
import at.studying.Response;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpMapper {
    public HttpMapper() {
    }

    public static Request toRequestObject(String httpRequest) {
        Request request = new Request();
        request.setMethod(getHttpMethod(httpRequest));
        request.setRoute(getRoute(httpRequest));
        parseAndAddAllHeaders(request, httpRequest);
        String contentLengthHeader = getHttpHeader("Content-Length", httpRequest);
        if (contentLengthHeader != null) {
            int contentLength = Integer.parseInt(contentLengthHeader);
            request.setContentLength(contentLength);
            if (contentLength > 0) {
                request.setBody(httpRequest.substring(httpRequest.length() - contentLength));
            }
        }

        return request;
    }

    private static void parseAndAddAllHeaders(Request request, String httpRequest) {
        Pattern headerPattern = Pattern.compile("^(.+?):\\s*(.+)$", 8);
        Matcher matcher = headerPattern.matcher(httpRequest);

        while(matcher.find()) {
            String headerName = matcher.group(1);
            String headerValue = matcher.group(2);
            request.addHeader(headerName, headerValue);
        }

    }

    public static String toResponseString(Response response) {
        String responseBody = response.getBody() != null ? response.getBody() : "";
        int contentLength = responseBody.length();
        int var10000 = response.getStatusCode();
        return "HTTP/1.1 " + var10000 + " " + response.getStatusMessage() + "\r\nContent-Type: " + response.getContentType() + "\r\nContent-Length: " + contentLength + "\r\n\r\n" + responseBody;
    }

    private static HttpMethod getHttpMethod(String httpRequest) {
        HttpMethod var10000;
        switch (httpRequest.split(" ")[0]) {
            case "GET":
                var10000 = HttpMethod.GET;
                break;
            case "POST":
                var10000 = HttpMethod.POST;
                break;
            case "PUT":
                var10000 = HttpMethod.PUT;
                break;
            case "DELETE":
                var10000 = HttpMethod.DELETE;
                break;
            default:
                throw new RuntimeException("No HTTP Method");
        }

        return var10000;
    }

    private static String getRoute(String httpRequest) {
        return httpRequest.split(" ")[1];
    }

    private static String getHttpHeader(String header, String httpRequest) {
        Pattern regex = Pattern.compile("^" + header + ":\\s(.+)", 8);
        Matcher matcher = regex.matcher(httpRequest);
        return !matcher.find() ? null : matcher.group(1);
    }
}
