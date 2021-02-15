package Settings;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ConfSettings {

    private static StringTokenizer tokenizer;

    private static HashMap<String, ArrayList<String>> configurationsDictionary = new HashMap<String, ArrayList<String>>();

    public static void init () throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader("conf/httpd.conf"));
        String line = "";
        while ((line = reader.readLine()) != null) {
            if(!line.isEmpty() && line.charAt(0) != '#') {
                assignTokens(line);
            }
        }
        //System.out.println(configurationsDictionary);
    }

    private static void assignTokens(String line) {
        tokenizer = new StringTokenizer(line);
        ArrayList<String> listOfTokens = new ArrayList<String>();
        while (tokenizer.hasMoreTokens()) {
            listOfTokens.add(tokenizer.nextToken());
        }
        String key = listOfTokens.remove(0); 
        configurationsDictionary.put(key, listOfTokens);
    }

    public static ArrayList<String> getValue(String option) {
        return configurationsDictionary.get(option);
    }

}