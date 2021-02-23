package Server.HttpMethods;

import Server.HttpResponse;
import Server.ParseHttpRequest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PutMethod extends HttpMethod{
    File file;
    FileWriter fw;
    private String statusCode;

    @Override
    public void execute(String filePath, HttpResponse response, ParseHttpRequest request) {
        file = new File(filePath);
        System.out.println(filePath);

        try {
            fw = new FileWriter(file, false);
            if (file.exists()) {
                fw.write(request.getBody());
                System.out.println("PUT existed " + request.getBody());
                statusCode = "200";
            } else {
                file.createNewFile();
                System.out.println("------- File Created -------");
                fw.write(request.getBody());
                System.out.println("PUT " + request.getBody());
                statusCode = "201";
            }
            fw.flush();
            fw.close();
        } catch(IOException e) {
            statusCode = "400";
        }
    }

    @Override
    public String getStatusCode() {
        return this.statusCode;
    }
}
