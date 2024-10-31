package at.studying;

import at.studying.User;
import at.studying.HttpContentType;
import at.studying.HttpStatus;
import at.studying.Request;
import at.studying.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.PrintStream;

public class UserController implements Controller {
    private final InMemoryUserDatabase userDatabase = new InMemoryUserDatabase();

    public UserController() {
    }

    public boolean supports(String route) {
        return route.startsWith("/users") || route.equals("/sessions") || route.equals("/stats");
    }

    public Response handle(Request request) {
        Response response = new Response();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String[] pathSegments = request.getRoute().split("/");

            if (request.getMethod().equals("POST") && request.getRoute().equals("/sessions")) {
                this.handleLogin(request, response, objectMapper);
                return response;
            }

            if (pathSegments.length >= 2 && pathSegments[1].equals("users")) {
                if (request.getMethod().equals("POST")) {
                    this.handleUserRegistration(request, response, objectMapper);
                }
                else if (request.getMethod().equals("GET")) {
                    this.handleUserRetrieval(request, response, objectMapper, pathSegments);
                } else {
                    this.setNotFoundResponse(response);
                }
            } else {
                this.setNotFoundResponse(response);
            }
        } catch (Exception var5) {
            this.setErrorResponse(response, var5);
        }

        return response;
    }

    private void handleLogin(Request request, Response response, ObjectMapper objectMapper) throws IOException {
        User loginUser = objectMapper.readValue(request.getBody(), User.class);
        System.out.println("Attempting login for username: " + loginUser.getUsername());
        User storedUser = this.userDatabase.getUser(loginUser.getUsername());
        if (storedUser != null) {
            boolean passwordMatch = loginUser.getPassword().equals(storedUser.getPassword());
            PrintStream var10000 = System.out;
            String var10001 = loginUser.getUsername();
            var10000.println("Password match for " + var10001 + ": " + passwordMatch);
            if (passwordMatch) {
                String token = this.generateToken(storedUser.getUsername());
                response.setStatus(HttpStatus.OK);
                response.setContentType(HttpContentType.APPLICATION_JSON);
                response.setBody("{\"message\": \"User Login successful\", \"token\": \"" + token + "\"}");
            } else {
                response.setStatus(HttpStatus.UNAUTHORIZED);
                response.setContentType(HttpContentType.APPLICATION_JSON);
                response.setBody("{\"error\": \"Invalid username or password\"}");
            }
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED);
            response.setContentType(HttpContentType.APPLICATION_JSON);
            response.setBody("{\"error\": \"Invalid username or password\"}");
        }
    }

    private void handleUserRegistration(Request request, Response response, ObjectMapper objectMapper) throws IOException {
        User newUser = objectMapper.readValue(request.getBody(), User.class);
        if (this.userDatabase.doesUsernameExist(newUser.getUsername())) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setContentType(HttpContentType.APPLICATION_JSON);
            response.setBody("{\"error\": \"User with same username already registered\"}");
        } else {
            this.userDatabase.createUser(newUser);
            response.setStatus(HttpStatus.OK);
            response.setContentType(HttpContentType.APPLICATION_JSON);
            response.setBody("{\"message\": \"User successfully created\"}");
        }
    }

    private void handleUserRetrieval(Request request, Response response, ObjectMapper objectMapper, String[] pathSegments) throws IOException {
        if (pathSegments.length != 3) {
            throw new IllegalArgumentException("Invalid URL format");
        } else {
            String username = pathSegments[2];
            User user = this.userDatabase.getUser(username);
            if (user != null) {
                response.setStatus(HttpStatus.OK);
                response.setContentType(HttpContentType.APPLICATION_JSON);
                response.setBody(objectMapper.writeValueAsString(user));
            } else {
                response.setStatus(HttpStatus.NOT_FOUND);
                response.setContentType(HttpContentType.APPLICATION_JSON);
                response.setBody("{\"error\": \"User not found\"}");
            }

        }
    }

    private void setNotFoundResponse(Response response) {
        response.setStatus(HttpStatus.NOT_FOUND);
        response.setContentType(HttpContentType.TEXT_PLAIN);
        response.setBody("Not Found");
    }

    private void setErrorResponse(Response response, Exception e) {
        response.setStatus(HttpStatus.BAD_REQUEST);
        response.setContentType(HttpContentType.APPLICATION_JSON);
        response.setBody("{\"error\": \"Error processing request: " + e.getMessage() + "\"}");
    }

    private String extractUsernameFromToken(String token) {
        String[] parts = token.split("-");
        return parts.length > 0 ? parts[0] : null;
    }

    private String generateToken(String username) {
        return username + "-mtcgToken";
    }
}