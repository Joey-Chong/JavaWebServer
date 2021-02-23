package Server;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.File;
import java.io.FileWriter;

import Dictionaries.ResponseDictionary;
import Dictionaries.MethodTable;
import Dictionaries.ConfSettings;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class HttpResponse {

    private PrintWriter out;
    private ParseHttpRequest requestParser;
    private OutputStream outputStream;

    private String statusCode;
    private String requestMethod;
    private String contentType;
    private String contentLength;
    private String responseBody;
    private byte[] responseByte;
    private String filePath;
    private String remoteAddress;
    private boolean isScript;

    public HttpResponse(Socket socket) throws IOException {
        outputStream = socket.getOutputStream();
        out = new PrintWriter(socket.getOutputStream(), true);
        isScript = false;
    }

    public void respond() throws IOException {
        out.print("\r\n");

        out.print(ResponseDictionary.getSupportedVersion() + " " + statusCode + " " + ResponseDictionary.getPhrase(statusCode) + "\r\n");

        if (statusCode == "401" || statusCode == "403") {
            out.print("WWW-Authenticate: Basic\r\n");
        }

        out.print("Date: " + printDate() + "\r\n");
        out.print(ResponseDictionary.getServerMessage() + "\r\n");

        if (requestMethod.equals("HEAD")) {
            out.print("Last-Modified: " + ResponseDictionary.getDateModified() + "\r\n");
        }

        if (!isScript) {
            out.print("Content-Type: " + contentType + "\r\n");
            out.print("Content-Length: " + contentLength + "\r\n");
        }

        out.print("Connection: Closed\r\n");

        if (!isScript) {
            out.print("\r\n");
        }

        out.flush();

        if (responseByte != null) {
            outputStream.write(responseByte);
            out.print("\r\n");
        }
        outputStream.flush();

        out.flush();
        writeToLog();
    }

    private String printDate() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("E, yyyy MM dd hh:mm:ss zzz");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(currentDate).toString();
    }

    private void writeToLog() {
        System.out.println("------- Logging -------");
        File logFile = new File(ConfSettings.getConfiguration("LogFile"));
        try {
            FileWriter logWriter = new FileWriter(logFile, true);
            String logLine = String.format("%s-%s-\"%s\"-%s", remoteAddress.toString(),printDate(),
                    requestMethod, statusCode);
            logWriter.write(logLine);
            logWriter.flush();
            System.out.println(logLine);
            logWriter.close();
        } catch (IOException exception) {
            System.out.println(">***** " + exception);
        }
        System.out.println("------- Logging Done -------");
    }

    public void setRemoteAddress(String address) {
        remoteAddress = address;
    }

    public String getRemoteAddress() {
        return remoteAddress;
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

    public String getContentType() {
        return this.contentType;
    }

    public void setContentLength(String length) {
        contentLength = length;
    }

    public void setResponseBody(String body) {
        responseBody = body;
    }

    public void setResponseByte(byte[] body) {
        responseByte = body;
    }

    public OutputStream getOutputStream() {
        return this.outputStream;
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
