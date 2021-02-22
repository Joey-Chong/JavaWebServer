package Server.HttpMethods;

import Server.HttpResponse;

public abstract class HttpMethod {
    public abstract void execute(String filePath, HttpResponse response);
    public abstract String getStatusCode();
}
