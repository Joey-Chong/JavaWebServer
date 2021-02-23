package Dictionaries;

import java.util.HashMap;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class ResponseDictionary {

    private static HashMap<String, String> phraseDictionary = new HashMap<String, String>();
    private static String dateModified = "E, yyyy MM dd hh:mm:ss zzz";
    private static String supportedVersion = "HTTP/1.1";
    private static final String serverMessage = "Server: Belaid, Chong";

    public static void init () {
        phraseDictionary.put("200", "OK");
        phraseDictionary.put("201", "Created");
        phraseDictionary.put("204", "No Content");
        phraseDictionary.put("304", "Not Modified");
        phraseDictionary.put("400", "Bad Request");
        phraseDictionary.put("401", "Unauthorized");
        phraseDictionary.put("403", "Forbidden");
        phraseDictionary.put("404", "Not Found");
        phraseDictionary.put("500", "Internal Server Error");
        updateDateModified();
    }

    public static String getPhrase(String code) {
        return phraseDictionary.get(code);
    }

    public static void updateDateModified() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("E, yyyy MM dd hh:mm:ss zzz");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        dateModified = dateFormat.format(currentDate);
    }

    public static String getDateModified() {
        return dateModified;
    }

    public static String getSupportedVersion() {
        return supportedVersion;
    }

    public static String getServerMessage() {
        return serverMessage;
    }
}