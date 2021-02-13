package Server;
import java.net.*;
import java.io.*;
import java.util.HashMap;

public class ServerInit {
    ParseHttpRequest parser;
    HttpResponse responser;

    //if possible, change the way IOException is thrown or caught
    public ServerInit() throws IOException {
        System.out.println("Server started");
        System.out.println("Preparing Mime Type");
        ContentTypeInit contentTypes = new ContentTypeInit();
        System.out.println("Starting Server");
        System.out.println("------------------------------------------");
        start();
    }

    private void start() throws IOException {
        ServerSocket socket = new ServerSocket(8080);   //this is default
        Socket client = null;

        while(true) {
            client = socket.accept();
            parser = new ParseHttpRequest(client);
            responser = new HttpResponse(parser);
//            readRequest(client);
//            sendResponse(client);
            client.close();
        }
    }

    //will be moved, this is just a prototype
    private void sendResponse(Socket client) throws IOException {
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        System.out.println("Sending response");
        out.print("HTTP/1.1 404 Not found\r\n");
//        out.print("HTTP/1.1 401 Unauthorized\r\n");
//        out.print("WWW-Authenticate: Basic\r\n");
        out.print("\r\n");
        out.flush();
        System.out.println("Sent");
    }
}