package handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ImageHandler {

    public void sendImage(OutputStream output, String imagePath) throws IOException {
        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            sendErrorResponse(output, "404 Not Found");
            return;
        }

        FileInputStream fileInputStream = new FileInputStream(imageFile);
        byte[] buffer = new byte[1024];
        int bytesRead;

        String response = "HTTP/1.1 200 OK\r\n";
        response += "Content-Type: image/png\r\n";
        response += "Content-Length: " + imageFile.length() + "\r\n";
        response += "\r\n";

        output.write(response.getBytes(StandardCharsets.UTF_8));

        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }

        fileInputStream.close();
        output.flush();
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