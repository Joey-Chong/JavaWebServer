package Server.HttpMethods;

import Server.HttpResponse;
import Server.ParseHttpRequest;

public abstract class HttpMethod {
    public abstract void execute(String filePath, HttpResponse response, ParseHttpRequest request);
    public abstract String getStatusCode();
}
