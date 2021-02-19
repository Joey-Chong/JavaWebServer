package Server;
import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.ArrayList;
import Dictionaries.ConfSettings;

public class ServerInit {
    ParseHttpRequest parser;
    HttpResponse responder;

    //if possible, change the way IOException is thrown or caught
    public ServerInit() throws IOException {
        System.out.println("Server started");
        System.out.println("Starting Server");
        System.out.println("------------------------------------------");
    }

    public void start(String port) throws IOException {
        ServerSocket socket = null;
        try {
            int intPort = Integer.parseInt(port);
            socket = new ServerSocket(intPort);
        } catch (NumberFormatException exception) {
            int defaultPort = Integer.parseInt(ConfSettings.getCoonfiguration("Listen").get(0));
            socket = new ServerSocket(defaultPort);
        }
        Socket client = null;

        while(true) {
            client = socket.accept();
            parser = new ParseHttpRequest(client);
            parser.handleRequest();
            responder = new HttpResponse(client, parser);
            responder.respond();
            client.close();
        }
    }
}