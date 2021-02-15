import Settings.ConfSettings;
import Settings.MimeSettings;
import Server.ServerInit;

import java.io.IOException;

public class WebServer {
  public static void main(String[] args) throws IOException {
    // This file will be compiled by script and must be at 
    // the root of your project directory
    
    try {
        ConfSettings.init();
        MimeSettings.init();
        ServerInit server = new ServerInit();
    } catch (IOException exception) {
        System.out.println("**** "+exception);
    } 
  }
}
