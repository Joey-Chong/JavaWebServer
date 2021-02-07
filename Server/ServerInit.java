package Server;
import java.net.*;
import java.io.*;

public class ServerInit {
    //if possible, change the way IOException is thrown or caught

    public ServerInit() throws IOException {
        System.out.println("Server started");
        start();
    }

    public void start() throws IOException {
        ServerSocket socket = new ServerSocket(8080);   //this is default
        Socket client = null;

        while(true) {
            client = socket.accept();
            readRequest(client);
        }
    }

    public void readRequest(Socket client) throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        while(true) {
            String line = reader.readLine();
            //debugger
            System.out.println(">" + line);
            //postman null debugging needed
            if(line == null) {
                break;
            }
            //indicate end of request or header, add body later
            if(line.equals("\r\n")) {
//                System.out.println("line break");
                break;
            }
        }
        //debugger
        System.out.println("Request completed");
    }
}