package Dictionaries;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class MimeSettings {

    private static StringTokenizer tokenizer;

    private static HashMap<String, String> typesDictionary = new HashMap<String, String>();

    public static void init () throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader("conf/mime.types"));
        String line = "";
        while ((line = reader.readLine()) != null) {
            if(!line.isEmpty() && line.charAt(0) != '#') {
                assignTokens(line);
            }
        }
    }

    private static void assignTokens(String line) {
        tokenizer = new StringTokenizer(line);
        ArrayList<String> listOfTokens = new ArrayList<String>();
        while (tokenizer.hasMoreTokens()) {
            listOfTokens.add(tokenizer.nextToken());
        }
        String type = listOfTokens.remove(0);
        for (String extension : listOfTokens) {
            typesDictionary.put(extension, type);
        }
    }

    public static String getType(String extension) {
        String type = typesDictionary.get(extension);
        if (type == NULL) {
            type = "text/text";
        }
        return type;
    }

}