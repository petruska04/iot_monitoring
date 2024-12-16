package handler;//Обрабатывает HTTP-запросы от клиента.

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RequestHandler {

    private final Socket clientSocket;
    private final Map<String, String> fileMap = new HashMap<>();

    public RequestHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        fileMap.put("/", "index.html");
        fileMap.put("/login", "login.html");
        fileMap.put("/register", "register.html");
        fileMap.put("/devices", "devices.html");
        fileMap.put("/style/style.css", "style/style.css");
        fileMap.put("/style/index.css", "style/index.css");
        fileMap.put("/style/login.css", "style/login.css");
        fileMap.put("/style/register.css", "style/register.css");
        fileMap.put("/style/devices.css", "style/devices.css");
        fileMap.put("/js/index.js", "js/index.js");
        fileMap.put("/js/login.js", "js/login.js");
        fileMap.put("/js/register.js", "js/register.js");
        fileMap.put("/js/devices.js", "js/devices.js");
        fileMap.put("/images/lightbulb.png", "images/lightbulb.png");
    }

    public void handleRequest() throws IOException {
        InputStream input = clientSocket.getInputStream();
        OutputStream output = clientSocket.getOutputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String requestLine = reader.readLine();
        System.out.println("Получен запрос: " + requestLine);

        String[] requestParts = requestLine.split(" ");
        String method = requestParts.length > 0 ? requestParts[0] : "";
        String path = requestParts.length > 1 ? requestParts[1] : "/";

        System.out.println(path);

        Map<String, String> headers = new HashMap<>();
        String headerLine;
        while ((headerLine = reader.readLine()) != null && !headerLine.isEmpty()) {
            String[] headerParts = headerLine.split(": ");
            if (headerParts.length == 2) {
                headers.put(headerParts[0], headerParts[1]);
            }
        }

        String body = null;
        if (method.equals("POST")) {
            String contentType = headers.get("Content-Type");
            int contentLength = Integer.parseInt(headers.getOrDefault("Content-Length", "0"));

            if (contentLength > 0) {
                char[] bodyChars = new char[contentLength];
                reader.read(bodyChars, 0, contentLength);
                body = new String(bodyChars);
            }

            System.out.println("Тело запроса: " + body);
        }

        if (fileMap.containsKey(path)) {
            String fileName = fileMap.get(path);
            String contentType = getContentType(fileName);

            if (fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
                ImageHandler imageHandler = new ImageHandler();
                imageHandler.sendImage(output, fileName);
            } else {
                sendFile(output, fileName, contentType);
            }
        } else if (path.startsWith("/auth")) {
            AuthHandler authHandler = new AuthHandler(output);
            authHandler.handleAuthRequest(path, body);
        } else {
            sendErrorResponse(output, "404 Not Found");
        }
    }

    private String getContentType(String fileName) {
        if (fileName.endsWith(".html")) {
            return "text/html";
        } else if (fileName.endsWith(".css")) {
            return "text/css";
        } else if (fileName.endsWith(".js")) {
            return "application/javascript";
        } else if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else {
            return "text/plain";
        }
    }

    private void sendFile(OutputStream output, String fileName, String contentType) throws IOException {
        String content = Utils.readFileFromResources(fileName);
        if (content != null) {
            String response = "HTTP/1.1 200 OK\r\n";
            response += "Content-Type: " + contentType + "; charset=UTF-8\r\n";
            response += "Content-Length: " + content.getBytes(StandardCharsets.UTF_8).length + "\r\n";
            response += "\r\n";
            response += content;

            output.write(response.getBytes(StandardCharsets.UTF_8));
            output.flush();
        } else {
            sendErrorResponse(output, "404 Not Found");
        }
    }

    private void sendErrorResponse(OutputStream output, String status) throws IOException {
        String response = "HTTP/1.1 " + status + "\r\n";
        response += "Content-Type: text/html; charset=UTF-8\r\n";
        response += "\r\n";
        response += "<h1>" + status + "</h1>";

        output.write(response.getBytes(StandardCharsets.UTF_8));
        output.flush();
    }
}