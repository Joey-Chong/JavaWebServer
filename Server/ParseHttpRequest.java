package Server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.ArrayList;
import Dictionaries.ResponseDictionary;

public class ParseHttpRequest {
    private String method;
    private String identifier;
    private String version;
    private HashMap<String, String> headerMap;
    private String statusCode;
    private String contentType;
    private String contentLength;
    private String responseBody;
    private String body;
    private BufferedReader reader;

    public ParseHttpRequest(Socket client) throws IOException {
        reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
    }

    public void handleRequest() throws IOException {
        String line;
        System.out.println("------- Header -------");

        System.out.println("------- Method -------");
        line = reader.readLine();
        System.out.println(">" + line);
        String[] methodLine = line.split(" ");

        if(methodLine.length != 3 && !methodLine[0].matches("GET|HEAD|POST|PUT|DELETE")) {
            statusCode = "400";
            printStatusCode(statusCode);
            //change to connection: close maybe?
            System.exit(1);
        }

        method = methodLine[0];
        identifier = methodLine[1];
        version = methodLine[2];
        version.replace("\\r\\n", "");
        if(!version.equals(ResponseDictionary.getSupportedVersion())) {
            statusCode = "400";
            printStatusCode(statusCode);
            //change to connection: close maybe?
            System.exit(1);
        }

        String[] headerLine;
        headerMap = new HashMap<String, String>();
        System.out.println("------- Rest of Headers -------");
        while((line = reader.readLine()).length() != 0) {
            System.out.println(">" + line);

            headerLine = line.split(": ");
            headerMap.put(headerLine[0], headerLine[1]);

            //if no Content-Length is given from client or server, it indicates there is no body
            if(line.equals("\\r\\n")) {
                //debugger
                System.out.println("------- End of header -------");
                break;
            }
        }

        if(headerMap.containsKey("Content-Type") && headerMap.containsKey("Content-Length")) {
            body = null;
            System.out.println("------- Body -------");
            while ((body = reader.readLine()) != null) {
                System.out.println(">" + body);
                if (body.equals("\\r\\n")) {
                    break;
                }
            }
            System.out.println("------- End of Body -------");
        }
        else {
            System.out.println("------- No Body Present -------");
        }

//        ---------- To print out key value pair of the headerMap ----------
//        System.out.println("----- Header Map Key-Value Pair -----");
//        for(String key : headerMap.keySet()){
//            System.out.println(key + ": " + headerMap.get(key));
//        }

        //needs to add other error code via checking, if not it will always be 200
        //thinking to remove this and let HttpResponse to handle statusCode instead
        statusCode = "200";
        printStatusCode(statusCode);

        return;
    }

    //left as public just in case needed in response
    public void printStatusCode(String statusCode) {
        System.out.println(statusCode + " " + ResponseDictionary.getPhrase(statusCode));
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

    public String getContentLength() {
        return this.contentLength;
    }

    public String getResponseBody() {
        return this.responseBody;
    }
}
