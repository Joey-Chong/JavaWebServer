package Server;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import Dictionaries.ResponseDictionary;
import Server.HttpMethods.HttpMethod;
import Server.HttpMethods.MethodTable;

import java.util.Date;
import java.text.SimpleDateFormat;

public class HttpResponse {

    private PrintWriter out;
    private ParseHttpRequest requestParser;
    private String requestMethod;
    private String requestMethodClass;

    public HttpResponse(Socket socket, ParseHttpRequest requestParser) throws IOException {
        out = new PrintWriter(socket.getOutputStream(), true);
        this.requestParser = requestParser;
        requestMethod = requestParser.getMethod();
        MethodTable.init();
        requestMethodClass = MethodTable.get(requestMethod);
    }

    public void respond() throws IOException {
        out.print("\r\n");

        String code = requestParser.getStatusCode();
        out.print(ResponseDictionary.getSupportedVersion() + " " + code + " " + ResponseDictionary.getPhrase(code) + "\r\n");
        out.print("WWW-Authenticate: Basic\r\n");
        printDate();
        out.print(ResponseDictionary.getServerMessage() + "\r\n");

        System.out.println("------------- Creating method instances -------------");
        try {
            HttpMethod httpMethod = (HttpMethod) (Class.forName("Server.HttpMethods." + requestMethodClass).newInstance());
            httpMethod.execute();
            code = httpMethod.getStatusCode();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        if (requestParser.getMethod().equals("HEAD")) {
            out.print("Last-Modified: " + ResponseDictionary.getDateModified() + "\r\n");
        }
        out.print("Content-Type: " + requestParser.getContentType() + "\r\n");
        out.print("Content-Length: " + requestParser.getContentLength() + "\r\n");
        out.print("Connection: Closed\r\n");

        out.print("\r\n");

        if (requestParser.getResponseBody() != null) {
            out.print(requestParser.getResponseBody() + "\r\n");
        }
        out.flush();
        Logger logger = new Logger(requestParser);
    }

    private void printDate() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("E, yyyy MM dd hh:mm:ss zzz");
        out.print("Date: "+dateFormat.format(currentDate)+"\r\n");
    }
}
/**
 HTTP_VERSION STATUS_CODE REASON_PHRASE  HTTP_HEADERS 
 
BODY

• HTTP_VERSION is the current HTTP version (ex. HTTP/1.1)
• STATUS_CODE is a code defined by the protocol that communicates the status of the response (we’ll look at some of the common status codes over the next few slides, all are available on wiki)
• REASON_PHRASE is the descriptive text corresponding to the status code
• BODY is the requested resource content (optional, only present if the Content-Length header is present)
**/