package Server.HttpMethods;

import Server.HttpResponse;
import Server.ParseHttpRequest;

import java.io.File;

public class HeadMethod extends HttpMethod{
    File file;

    @Override
    public void execute(String filePath, HttpResponse responder, ParseHttpRequest request) {
        file = new File(filePath);

        responder.setContentLength(String.valueOf(file.length()));
        responder.setStatusCode("200");
    }
}
