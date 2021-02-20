package Server;

import java.util.HashMap;
import java.util.Base64;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.nio.Buffer;

public class Authenticator {

    private HashMap<String, String> passwordsDictionary = new HashMap<String, String>();
    private HashMap<String, String> authSettings = new HashMap<String, String>();

    public Authenticator(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line = "";
        while ((line = reader.readLine()) != null) {
            if(!line.isEmpty() && line.charAt(0) != '#') {
                assignSettings(line);
            }
        }
        reader = new BufferedReader(new FileReader(authSettings.get("AuthUserFile")));
        line = "";
        while ((line = reader.readLine()) != null) {
            if(!line.isEmpty() && line.charAt(0) != '#') {
                assignTokens(line);
            }
        }
    }

    private void assignSettings(String line) {
        StringTokenizer tokenizer = new StringTokenizer(line);
        ArrayList<String> listOfTokens = new ArrayList<String>();
        while (tokenizer.hasMoreTokens()) {
            listOfTokens.add(tokenizer.nextToken());
        }
        authSettings.put(listOfTokens.get(0), listOfTokens.get(1));
    }

    private void assignTokens(String line) {
        String[] tokens = line.split(":");
        if(tokens.length == 2) {
            passwordsDictionary.put(tokens[0], tokens[1].replace("{SHA}","").trim());
        }
    }

    public boolean checkAuthorization(String authInfo) {
        String credentials = new String(
            Base64.getDecoder().decode(authInfo),
            Charset.forName("UTF-8")
        );
        String[] tokens = credentials.split(":");
        return verifyPassword(tokens[0], tokens[1]);
    }

    private boolean verifyPassword(String username, String password) {
        String assignedPassword = passwordsDictionary.get(username);
        if (assignedPassword == null) {
            return false;
        }
        String passwordGiven = encryptClearPassword(password);
        if (passwordGiven == assignedPassword) {
            return true;
        } 
        return false;
    }

    private String encryptClearPassword(String password) {
        try {
            MessageDigest mDigest = MessageDigest.getInstance("SHA-1");
            byte[] result = mDigest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(result);
        } catch(Exception e) {
            return "";
        }
    }
}