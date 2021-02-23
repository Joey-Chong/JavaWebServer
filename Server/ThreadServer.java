package Server;

import java.net.Socket;
import java.lang.Thread;
import java.io.IOException;
 
public class ThreadServer extends Thread {
    private ParseHttpRequest parser;
    private HttpResponse responder;
    private Socket threadSocket = null;
 
    public ThreadServer(Socket socket) {
        this.threadSocket = socket;
    }
     
    public void run() {
        try {
            responder = new HttpResponse(threadSocket);
            parser = new ParseHttpRequest(threadSocket, responder);
            parser.handleRequest();
            responder.respond();
            threadSocket.close();
        } catch (Exception exception) {
            try {
                responder.setStatusCode("500");
                responder.respond();
            } catch (IOException ioexception) {
                System.out.println(">***** " + ioexception);
            }
        }
    }
}