package Server;
import java.net.*;
import java.io.*;

public class ServerInit {
    //if possible, change the way IOException is thrown or caught
    public static void start() throws IOException {
        ServerSocket socket = new ServerSocket(8080);   //this is default
        Socket client = null;

        while(true) {
            client = socket.accept();
            readRequest(client);
        }
    }

    public static void readRequest(Socket client) throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        while(true) {
            String line = reader.readLine();
            //debugger
            System.out.println(">" + line);
            //indicate end of request or header, add body later
            if(line.equals("\\r\\n")) {
                //debugger
                System.out.println("End of request");
                break;
            }
        }
        //debugger
        System.out.println("Request completed");
    }
}