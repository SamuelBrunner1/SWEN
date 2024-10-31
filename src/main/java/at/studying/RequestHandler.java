package at.studying;

import at.studying.Request;
import at.studying.Response;
import at.studying.HttpMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestHandler {
    private BufferedReader in;
    private PrintWriter out;
    private final Socket client;
    private final ServerApplication app;

    public RequestHandler(Socket client, ServerApplication app) {
        this.client = client;
        this.app = app;
    }

    public void handle() throws IOException {
        this.in = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
        String httpRequest = this.getHttpStringFromStream(this.in);
        Request request = HttpMapper.toRequestObject(httpRequest);
        Response response = this.app.handle(request);
        this.out = new PrintWriter(this.client.getOutputStream(), true);
        this.out.write(HttpMapper.toResponseString(response));
        this.out.close();
        this.in.close();
        this.client.close();
    }

    private String getHttpStringFromStream(BufferedReader in) throws IOException {
        StringBuilder builder = new StringBuilder();

        String inputLine;
        while((inputLine = in.readLine()) != null && !inputLine.equals("")) {
            builder.append(inputLine).append(System.lineSeparator());
        }

        String httpRequest = builder.toString();
        Pattern regex = Pattern.compile("^Content-Length:\\s(.+)", 8);
        Matcher matcher = regex.matcher(httpRequest);
        if (!matcher.find()) {
            return builder.toString();
        } else {
            builder.append(System.lineSeparator());
            int contentLength = Integer.parseInt(matcher.group(1));
            char[] buffer = new char[contentLength];
            in.read(buffer, 0, contentLength);
            builder.append(buffer);
            return builder.toString();
        }
    }
}