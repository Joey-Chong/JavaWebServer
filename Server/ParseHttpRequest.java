package Server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;
import Dictionaries.ResponseDictionary;
import Dictionaries.MimeSettings;
import Dictionaries.MethodTable;
import Server.HttpMethods.HttpMethod;
import java.io.File;
import java.util.Locale;

public class ParseHttpRequest {
    private String remoteAddress;

    private String method;
    private String identifier;
    private String version;
    private HashMap<String, String> headerMap;
    private String contentType;
    private int contentLength;
    private String body;
    private BufferedReader reader;
    private boolean hasBody;

    private HttpResponse responder;

    public ParseHttpRequest(Socket client, HttpResponse responder) throws IOException {
        this.remoteAddress = client.getRemoteSocketAddress().toString();
        this.responder = responder;
        reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        headerMap = new HashMap<String, String>();
    }

    public void handleRequest() throws IOException {

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

        if (!FilePathing.checkFileExists(serverPath) && !method.equals("PUT")) {
            responder.setStatusCode("404");
            return;
        }

        assignFileType(serverPath);

        responder.setFilePath(serverPath);
        if (!FilePathing.checkFileScriptAliased(identifier)) {
            if (!prepareResponse(serverPath)) {
                return;
            }
        } else {
            responder.setIsScript();
            String queryString = FilePathing.getQueryString(identifier);
            if (!CGIScript.executeScript(serverPath, queryString, this)) {
                return;
            }
        }
        System.out.println("------- Parsing Request Done -------");

        return;
    }

    private boolean handleMethodLine() throws IOException {
        String line;
        System.out.println("------- Method Headers -------");
        line = reader.readLine();
        System.out.println(">" + line);
        String[] methodLine = line.split(" ");

        if(methodLine.length != 3 || !methodLine[0].matches("GET|HEAD|POST|PUT|DELETE")) {
            responder.setStatusCode("400");
            return false;
        }

        responder.setRequestMethod(methodLine[0]);

        method = methodLine[0];
        identifier = methodLine[1];
        version = methodLine[2];
        version.replace("\\r\\n", "");
        if(!version.equals(ResponseDictionary.getSupportedVersion())) {
            responder.setStatusCode("400");
            return false;
        }
        return true;
    }

    private boolean handleHeaders() throws IOException {
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
            if (headerLine[0].equals("Authorization")) {
                String[] authComponents = headerLine[1].split(" ");
                headerMap.put(headerLine[0], authComponents[1]);
            } else {
                headerMap.put(headerLine[0], headerLine[1]);
            }

            for (String name: headerMap.keySet()){
                String key = name.toString();
                String value = headerMap.get(name).toString();
                System.out.println(key + " " + value);
            }

        }
        return true;
    }

    private boolean handleBody() throws IOException {
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
                    body += bodyReader + "\n";
                }
            }
            System.out.println("------- End of Body -------");
            if(body.length() != contentLength) {
                System.out.println("------- Body length does not match Content-Length -------");
                responder.setStatusCode("400");
                return false;
            }
        }
        else {
            hasBody = false;
            System.out.println("------- No Body Present -------");
        }
        return true;
    }

    private boolean handleAuthorization(String accessFilePath) throws IOException {
        if (headerMap.get("Authorization") == null) {
            responder.setStatusCode("401");
            return false;
        }
        Authenticator auth = new Authenticator(accessFilePath);
        if (!auth.checkAuthorization(headerMap.get("Authorization"))) {
            responder.setStatusCode("403");
            return false;
        }
        return true;
    }

    private boolean handleAccessFiles(String absolutePath) throws IOException {
        String previousAccessFilePath = FilePathing.checkAuthRequired(absolutePath, "");
        while (previousAccessFilePath != null) {
            if (!handleAuthorization(previousAccessFilePath)) {
                return false;
            }
            previousAccessFilePath = FilePathing.checkAuthRequired(absolutePath, previousAccessFilePath);
        }
        return true;
    }

    private void assignFileType(String path) {
        File pathFile = new File(path);
        String fileName = pathFile.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
            String fileType = MimeSettings.getType(extension);
            responder.setContentType(fileType);
        }
    }

    private boolean prepareResponse(String serverPath) {
        System.out.println("------------- Creating method instances -------------");
        try {
            MethodTable.init();
            String requestMethodClass = MethodTable.get(method);
            System.out.println(requestMethodClass);
            HttpMethod httpMethod = (HttpMethod) (Class.forName("Server.HttpMethods." + requestMethodClass).newInstance());
            httpMethod.execute(serverPath, responder, this);
            responder.setStatusCode(httpMethod.getStatusCode());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            responder.setStatusCode("500");
            return false;
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

    public HttpResponse getResponder() {
        return this.responder;
    }

    public String getBody() {
        return this.body;
    }

    public String getContentType() {
        return this.contentType;
    }

    public int getContentLength() {
        return this.contentLength;
    }

    public boolean getHasBody() {
        return this.hasBody;
    }

    public String getRemoteAddress() {
        return this.remoteAddress;
    }
}
