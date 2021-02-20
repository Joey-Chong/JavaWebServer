import Dictionaries.*;
import Server.ServerInit;

import java.io.IOException;


import java.util.Base64;
import java.nio.charset.Charset;
import java.security.MessageDigest;

public class WebServer {
  public static void main(String[] args) throws IOException {
    // This file will be compiled by script and must be at
    // the root of your project directory
    /**
    String password = "server:hello";
    try {
        MessageDigest mDigest = MessageDigest.getInstance("SHA-1");
        byte[] result = mDigest.digest(password.getBytes());
        String coded = Base64.getEncoder().encodeToString(password.getBytes());
        System.out.println(coded);
        String credentials = new String(
            Base64.getDecoder().decode(coded),
            Charset.forName("UTF-8")
        );
        System.out.println(credentials);
    } catch(Exception exception) {
            System.out.println("**** "+exception);
    }
    **/
    try {
        ConfSettings.init();
        MimeSettings.init();
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