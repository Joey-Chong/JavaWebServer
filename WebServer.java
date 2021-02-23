import Dictionaries.*;
import Server.ThreadServer;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;

public class WebServer {
    public static void main(String[] args) throws IOException {
        ConfSettings.init();
        MimeSettings.init();
        ResponseDictionary.init();

        ServerSocket socket = null;

        try {
            int intPort = Integer.parseInt(args[0]);
            socket = new ServerSocket(intPort);
        } catch (ArrayIndexOutOfBoundsException exception) {
            int defaultPort = Integer.parseInt(ConfSettings.getConfiguration("Listen"));
            socket = new ServerSocket(defaultPort);
        }


        System.out.println("------------------Server Started------------------------");
        Socket client = null;
        while (true) {
            client = socket.accept();
            (new ThreadServer(client)).start();
        }
    }
}