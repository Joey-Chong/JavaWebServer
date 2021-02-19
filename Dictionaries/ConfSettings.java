package Dictionaries;

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

    private static HashMap<String, String> configurationsDictionary = new HashMap<String, String>();
    private static HashMap<String, String> aliasDictionary = new HashMap<String, String>();
    private static HashMap<String, String> scriptAliasDictionary = new HashMap<String, String>();

    public static void init () throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader("conf/httpd.conf"));
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
        String key = listOfTokens.remove(0); 
        if (key == "Alias") {
            aliasDictionary.put(listOfTokens[0], listOfTokens[1]);
        } else if (key == "ScriptAlias") {
            scriptAliasDictionary.put(listOfTokens[0], listOfTokens[1]);
        }
        configurationsDictionary.put(key, listOfTokens[0]);
    }

    public static String getConfiguration(String option) {
        return configurationsDictionary.get(option);
    }

    public static String getAlias(String option) {
        return aliasDictionary.get(option);
    }

    public static String getScriptAlias(String option) {
        return scriptAliasDictionary.get(option);
    }

}