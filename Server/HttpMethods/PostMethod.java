package Server.HttpMethods;

import Dictionaries.MimeSettings;
import Server.HttpResponse;
import Server.ParseHttpRequest;
import Dictionaries.ResponseDictionary;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PostMethod extends HttpMethod{
    File file;
    FileWriter fw;

    @Override
    public void execute(String filePath, HttpResponse responder, ParseHttpRequest request) {
        file = new File(filePath);

        try {
            fw = new FileWriter(file, true);
            System.out.println(filePath);
            fw.write(request.getBody());
            System.out.println("POST existed " + request.getBody());
            responder.setStatusCode("200");
            fw.flush();
            fw.close();
            responder.setContentLength(String.valueOf(file.length()));
            ResponseDictionary.updateDateModified();
        } catch(IOException e) {
            responder.setStatusCode("500");
        }
    }
}
