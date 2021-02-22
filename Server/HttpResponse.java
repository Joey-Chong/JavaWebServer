package Server;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import Dictionaries.ResponseDictionary;
import Dictionaries.MethodTable;

import java.util.Date;
import java.text.SimpleDateFormat;

public class HttpResponse {

    private PrintWriter out;
    private ParseHttpRequest requestParser;


    private String statusCode;
    private String requestMethod;
    private String contentType;
    private String contentLength;
    private String responseBody;
    private String filePath;
    private boolean isScript;

    public HttpResponse(Socket socket) throws IOException {
        out = new PrintWriter(socket.getOutputStream(), true);
        statusCode = "200";
        isScript = false;
    }

    public void respond() throws IOException {
        out.print("\r\n");

        out.print(ResponseDictionary.getSupportedVersion() + " " + statusCode + " " + ResponseDictionary.getPhrase(statusCode) + "\r\n");

        if (statusCode == "401" || statusCode == "403") {
            out.print("WWW-Authenticate: Basic\r\n");
        }

        printDate();
        out.print(ResponseDictionary.getServerMessage() + "\r\n");

        if (requestMethod.equals("HEAD")) {
            out.print("Last-Modified: " + ResponseDictionary.getDateModified() + "\r\n");
        }

        if (!isScript) {
            out.print("Content-Type: " + contentType + "\r\n");
            out.print("Content-Length: " + contentLength + "\r\n");
        }

        out.print("Connection: Closed\r\n");

        if (responseBody != null) {
            out.print(responseBody + "\r\n");
        }

        /**if (filePath != null) {
            File outFile = new File(filePath);
            out.print(filePath + "\r\n");
        }**/
        out.flush();
        //Logger logger = new Logger(requestParser);
    }

    private void printDate() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("E, yyyy MM dd hh:mm:ss zzz");
        out.print("Date: " + dateFormat.format(currentDate) + "\r\n");
    }

    public void setStatusCode(String newCode) {
        statusCode = newCode;
    }

    public void setRequestMethod(String method) {
        requestMethod = method;
    }

    public void setContentType(String type) {
        contentType = type;
    }

    public void setContentLength(String length) {
        contentLength = length;
    }

    public void setResponseBody(String body) {
        responseBody = body;
    }

    public void setFilePath(String path) {
        filePath = path;
    }

    public void setIsScript() {
        isScript = true;
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