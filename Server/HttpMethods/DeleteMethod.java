package Server.HttpMethods;

import Server.HttpResponse;
import Server.ParseHttpRequest;
import Dictionaries.ResponseDictionary;

import java.io.File;

public class DeleteMethod extends HttpMethod{
    File file;

    @Override
    public void execute(String filePath, HttpResponse responder, ParseHttpRequest request) {
        file = new File(filePath);
        file.delete();
        responder.setStatusCode("204");
        responder.setContentLength("0");
        ResponseDictionary.updateDateModified();
    }
}
