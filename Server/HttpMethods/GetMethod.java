package Server.HttpMethods;

import Server.FilePathing;
import Server.HttpResponse;
import Server.ParseHttpRequest;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GetMethod extends HttpMethod{
    private File file;
    OutputStream outputStream;

    @Override
    public void execute(String filePath, HttpResponse responder, ParseHttpRequest request) {
        System.out.println("Creating instance GET");

        outputStream = responder.getOutputStream();

        file = new File(filePath);
        responder.setContentLength(String.valueOf(file.length()));
        System.out.println(file.length());
        System.out.println(filePath);

        String contentType = responder.getContentType();
        System.out.println("content type: " + contentType);
        try {
            byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
            responder.setResponseByte(fileBytes);
            responder.setStatusCode("200");
            System.out.println("file bytes here: " + fileBytes);
        } catch (IOException e) {
            e.printStackTrace();
            responder.setStatusCode("500");
        }
    }
}