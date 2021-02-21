package Server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;
import Dictionaries.ResponseDictionary;

public class ParseHttpRequest {
    private String remoteAddress;
    private String method;
    private String identifier;
    private String version;
    private HashMap<String, String> headerMap;
    private String statusCode;
    private String contentType;
    private int contentLength;
    private String responseBody;
    private String body;
    private BufferedReader reader;
    private boolean hasBody;

    public ParseHttpRequest(Socket client) throws IOException {
        this.remoteAddress = client.getRemoteSocketAddress().toString();
        reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        headerMap = new HashMap<String, String>();
    }

    public void handleRequest() throws IOException {

        statusCode = "200";

        if (!handleMethodLine()) {
            return;
        }
        if (!handleHeaders()) {
            return;
        }
        if (!handleBody()) {
            return;
        }

        String serverPath = FilePathing.handlePathing(identifier);
        if (!handleAccessFiles(serverPath)) {
            return;
        }
        //needs to add other error code via checking, if not it will always be 200
        //thinking to remove this and let HttpResponse to handle statusCode instead
        printStatusCode(statusCode);
        System.out.println("------- Parsing Request Done -------");

        return;
    }

    public boolean handleMethodLine() throws IOException {
        String line;
        System.out.println("------- Method Headers -------");
        line = reader.readLine();
        System.out.println(">" + line);
        String[] methodLine = line.split(" ");

        if(methodLine.length != 3 || !methodLine[0].matches("GET|HEAD|POST|PUT|DELETE")) {
            statusCode = "400";
            printStatusCode(statusCode);
            return false;
        }

        method = methodLine[0];
        identifier = methodLine[1];
        version = methodLine[2];
        version.replace("\\r\\n", "");
        if(!version.equals(ResponseDictionary.getSupportedVersion())) {
            statusCode = "400";
            printStatusCode(statusCode);
            return false;
        }
        return true;
    }

    public boolean handleHeaders() throws IOException {
        String line;
        String[] headerLine;
        System.out.println("------- Rest of Headers -------");
        while((line = reader.readLine()).length() != 0) {
            System.out.println(">" + line);

            //if no Content-Length is given from client or server, it indicates there is no body
            if(line.equals("\\r\\n")) {
                //debugger
                System.out.println("------- End of header -------");
                break;
            }

            headerLine = line.split(": ");
            headerMap.put(headerLine[0], headerLine[1]);
        }
        return true;
    }

    public boolean handleBody() throws IOException {
        if(headerMap.containsKey("Content-Type") && headerMap.containsKey("Content-Length")) {
            String bodyReader;
            contentType = headerMap.get("Content-Type");
            contentLength = Integer.parseInt(headerMap.get("Content-Length"));
            body = "";
            hasBody = true;

            System.out.println("------- Body -------");
            while ((bodyReader = reader.readLine()) != null) {
                System.out.println(">" + bodyReader);
                if (bodyReader.equals("\\r\\n")) {
                    break;
                }
                else {
                    body = body + bodyReader + "\n";
                }
            }
            System.out.println("------- End of Body -------");
            if(body.length() != contentLength) {
                System.out.println("------- Body length does not match Content-Length -------");
                statusCode = "400";
                return false;
            }
            System.out.println(body);
        }
        else {
            hasBody = false;
            System.out.println("------- No Body Present -------");
        }
        return true;
    }

    //left as public just in case needed in response
    public void printStatusCode(String statusCode) {
        System.out.println(">" + statusCode + " " + ResponseDictionary.getPhrase(statusCode));
    }

    public boolean handleAuthorization(String accessFilePath) throws IOException {
        if (headerMap.get("Authorization") == null) {
            statusCode = "401";
            return false;
        }
        Authenticator auth = new Authenticator(accessFilePath);
        if (!auth.checkAuthorization(headerMap.get("Authorization"))) {
            statusCode = "403";
            return false;
        }
        return true;
    }
    public boolean handleAccessFiles(String absolutePath) throws IOException {
        String previousAccessFilePath = FilePathing.checkAuthRequired(absolutePath, "");
        while (previousAccessFilePath != null) {
            if (!handleAuthorization(previousAccessFilePath)) {
                return false;
            }
            previousAccessFilePath = FilePathing.checkAuthRequired(absolutePath, previousAccessFilePath);
        }
        return true;
    }

    public String getMethodLine() {
        return this.method + " " + this.identifier + " " + this.version;
    }

    public String getMethod() {
        return this.method;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public String getVersion() {
        return this.version;
    }

    public HashMap getHeaderMap() {
        return this.headerMap;
    }

    public String getBody() {
        return this.body;
    }

    public String getStatusCode() {
        return this.statusCode;
    }

    public String getContentType() {
        return this.contentType;
    }

    public int getContentLength() {
        return this.contentLength;
    }

    public String getResponseBody() {
        return this.responseBody;
    }

    public boolean getHasBody() {
        return this.hasBody;
    }

    public String getRemoteAddress() {
        return this.remoteAddress;
    }
}
