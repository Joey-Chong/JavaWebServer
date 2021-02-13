package Server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;

public class ParseHttpRequest {
    private String method;
    private String identifier;
    private String version;
    private HashMap headerMap;
    private String body;

    public ParseHttpRequest(Socket client) throws IOException {
        parseRequest(client);
    }

    private void parseRequest(Socket client) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        String line = null;
        System.out.println("------- Header -------");
        System.out.println("------- Method -------");
        line = reader.readLine();
        System.out.println(line);
        //below add parsing http method

        System.out.println("------- Rest of Headers -------");
        while((line = reader.readLine()).length() != 0) {
            System.out.println(">" + line);
            //if no Content-Length is given from client or server, it indicates there is no body
            if((line == null) || line.equals("\\r\\n")) {
                //debugger
                System.out.println("------- End of header -------");
                break;
            }
        }
        String body = null;
        System.out.println("------- Body -------");
        while((body = reader.readLine()) != null) {
            System.out.println(">" + body);
            if(body.equals("\\r\\n")) {
                break;
            }
        }
        System.out.println("------- End of body -------");
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
}
