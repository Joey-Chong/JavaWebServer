package Server.HttpMethods;

import Server.HttpResponse;
import Server.ParseHttpRequest;

import java.io.File;

public class DeleteMethod extends HttpMethod{
    private String statusCode;
    File file;

    @Override
    public void execute(String filePath, HttpResponse response, ParseHttpRequest request) {
        file = new File(filePath);
        if(file.exists()) {
            file.delete();
            statusCode = "204";
        }
        else {
            statusCode = "404";
        }
    }

    @Override
    public String getStatusCode() {
        return this.statusCode;
    }
}
