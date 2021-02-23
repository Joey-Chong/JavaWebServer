package Server.HttpMethods;

import Server.FilePathing;
import Server.HttpResponse;
import Server.ParseHttpRequest;
import Dictionaries.ConfSettings;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.BufferedReader;

public class GetMethod extends HttpMethod{
    private File file;
    OutputStream outputStream;

    @Override
    public void execute(String filePath, HttpResponse responder, ParseHttpRequest request) {
        System.out.println("Creating instance GET");

        /**if (checkChange(responder.getRemoteAddress())) {
            responder.setStatusCode("304");
            responder.setContentLength("0");
            return;
        }**/

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

    private boolean checkChange(String clientHost) {
        File logFile = new File(ConfSettings.getConfiguration("LogFile"));
        try {
            BufferedReader reader = new BufferedReader(new FileReader(logFile));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split("-");
                String[] logTokens = tokens[0].split(":");
                String logHost = "";
                for (int i = 0; i < logTokens.length -1; i++) {
                    logHost += logTokens[i];
                }
                String shortenedClientHost = "";
                String[] shortTokens = clientHost.split(":");
                for (int i = 0; i < shortTokens.length -1; i++) {
                    shortenedClientHost += shortTokens[i];
                }
                System.out.println(logHost);
                System.out.println(shortenedClientHost);
                System.out.println(tokens[2]);
                if (logHost.equals(shortenedClientHost) && (tokens[2].equals("GET") || tokens[2].equals("HEAD"))) {
                        return true;
                }
            }
        } catch (IOException exception) {
            return false;
        }
        return false;
    }
}