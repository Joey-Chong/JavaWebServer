package Server.HttpMethods;

import Dictionaries.MimeSettings;
import Server.HttpResponse;
import Server.ParseHttpRequest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PostMethod extends HttpMethod{
    File file;
    FileWriter fw;
    private String statusCode;

    @Override
    public void execute(String filePath, HttpResponse response, ParseHttpRequest request) {
        file = new File(filePath);

        try {
            fw = new FileWriter(file, true);
            if (file.exists()) {
                System.out.println(filePath);
                System.out.println(MimeSettings.getExtension(request.getContentType()));
                file.createNewFile();
                fw.write(request.getBody());
                System.out.println("POST existed " + request.getBody());
                statusCode = "200";
            }
            else {
                statusCode = "404";
            }
            fw.flush();
            fw.close();
        } catch(IOException e) {
            statusCode = "404";
        }
    }

    @Override
    public String getStatusCode() {
        return this.statusCode;
    }
}
