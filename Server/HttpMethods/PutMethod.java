package Server.HttpMethods;

import Server.HttpResponse;
import Server.ParseHttpRequest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PutMethod extends HttpMethod{
    private String newFilePath;
    File file;
    FileWriter fw;
    private String statusCode;

    @Override
    public void execute(String filePath, HttpResponse response, ParseHttpRequest request) {
        newFilePath = filePath;
        System.out.println(newFilePath);
        //test content type and extension in parser
        file = new File(newFilePath);

        try {
            fw = new FileWriter(file, false);
            if (file.isFile()) {
                fw.write(request.getBody());
                System.out.println("PUT existed " + request.getBody());
                statusCode = "400";
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
