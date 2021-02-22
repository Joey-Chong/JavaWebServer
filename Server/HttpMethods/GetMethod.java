package Server.HttpMethods;

import Server.FilePathing;
import Server.HttpResponse;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GetMethod extends HttpMethod{
    private File file;
    OutputStream outputStream;

    @Override
    public void execute(String filePath, HttpResponse response) {
        System.out.println("Creating instance GET");

        outputStream = response.getOutputStream();

        file = new File(filePath);
        response.setContentLength(String.valueOf(file.length()));
//        response.setContentLength("3077");
        System.out.println(file.length());
        System.out.println(filePath);

        String contentType = response.getContentType();
//        if(contentType.contains("image")) {
            System.out.println("content type: " + contentType);
            try {
                byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
                response.setResponseByte(fileBytes);
                System.out.println("file bytes here: " + fileBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
//        }
//        else {
//            try {
//                String body = "";
//                BufferedReader reader = new BufferedReader(new FileReader(file));
//                String line = "";
//                while ((line = reader.readLine()) != null) {
//                    body += line + "\n";
//                }
//                response.setResponseBody(body);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }

    @Override
    public String getStatusCode() {
        return null;
    }
}