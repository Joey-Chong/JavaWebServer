import Dictionaries.*;
import Server.ServerInit;

import java.io.IOException;

public class WebServer {
  public static void main(String[] args) throws IOException {
    // This file will be compiled by script and must be at 
    // the root of your project directory
    
    try {
        ConfSettings.init();
        MimeSettings.init();
        Athenticator.init();
        ResponseDictionary.init();
        ServerInit server = new ServerInit();
        try {
            server.start(args[0]);
        } catch (ArrayIndexOutOfBoundsException exception) {
            server.start(null);
        } 
    } catch (IOException exception) {
        System.out.println("**** "+exception);
    } 
  }
}
