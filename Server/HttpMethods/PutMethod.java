package Server.HttpMethods;

import Server.HttpResponse;
import Server.ParseHttpRequest;
import Dictionaries.ResponseDictionary;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PutMethod extends HttpMethod{
    File file;
    FileWriter fw;
    private String statusCode;

    @Override
    public void execute(String filePath, HttpResponse responder, ParseHttpRequest request) {

        file = new File(filePath);

        try {
            if (file.createNewFile()) {
                System.out.println("------- File Created -------");
                responder.setStatusCode("201");
            } else {
                System.out.println("------- File Existed -------");
                responder.setStatusCode("200");
            }
            fw = new FileWriter(file, false);
            fw.write(request.getBody());
            fw.flush();
            fw.close();
            responder.setContentLength(String.valueOf(file.length()));
            ResponseDictionary.updateDateModified();
        } catch(IOException e) {
            responder.setStatusCode("500");
        }
    }
}
