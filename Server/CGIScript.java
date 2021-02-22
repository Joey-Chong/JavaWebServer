package Server;

import java.io.File;
import Dictionaries.ConfSettings;
import Dictionaries.ResponseDictionary;
import java.lang.ProcessBuilder;
import java.util.Map;
import java.lang.ProcessBuilder.Redirect;
import java.io.FileReader;
import java.util.Iterator;
import java.util.Map.Entry;
import java.io.PrintWriter;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class CGIScript {

    public static boolean executeScript(String scriptPath, ParseHttpRequest parser) {
        try {
            ProcessBuilder operatingBuilder = new ProcessBuilder(scriptPath);
            Map<String, String> environmentMap = operatingBuilder.environment();
            if (!assignEnvironmentVariables(environmentMap, parser.getHeaderMap())) {
                parser.getResponder().setStatusCode("500");
                return false;
            }
            //File log = new File("log");
            //PrintWriter writer = new PrintWriter(log);
            //writer.print("");
            //writer.close();
            //operatingBuilder.redirectErrorStream(true);
            //operatingBuilder.redirectOutput(Redirect.appendTo(log));
            Process operation = operatingBuilder.start();
            //assert operatingBuilder.redirectInput() == Redirect.PIPE;
            //assert operatingBuilder.redirectOutput().file() == log;
            //assert operation.getInputStream().read() == -1;

            if (!redirectOutput(operation, parser.getResponder())) {
                parser.getResponder().setStatusCode("500");
                return false;
            }
            
            /**BufferedReader reader = new BufferedReader(new FileReader(log));
            String line = "";
            while ((line = reader.readLine()) != null) {
                body += line + "\n";
            }**/
        } catch (Exception e) {
            parser.getResponder().setStatusCode("500");
            return false;
        }

        return true;
    }

    private static boolean assignEnvironmentVariables(Map<String, String> environmentMap, HashMap<String, String> headerMap) {
        try {
            environmentMap.put("QUERY_STRING", "query1=a&query2=b");
            environmentMap.put("SERVER_PROTOCOL", ResponseDictionary.getSupportedVersion());
            Iterator headerIterator = headerMap.entrySet().iterator(); 
            while (headerIterator.hasNext()) { 
                Map.Entry mapElement = (Map.Entry)headerIterator.next(); 
                String key = "HTTP_" + ((String)mapElement.getKey()).toUpperCase();
                environmentMap.put(key, (String)mapElement.getKey());
            } 
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private static boolean redirectOutput(Process operation, HttpResponse responder) {
        try {
            BufferedReader redirectReader = new BufferedReader(new InputStreamReader(operation.getInputStream()));
            StringBuilder bodyBuilder = new StringBuilder();
            String body = "";
            while ((body = redirectReader.readLine()) != null) {
                bodyBuilder.append(body);
                bodyBuilder.append("\n");
            }
            String outputBody = bodyBuilder.toString();
            responder.setResponseBody(outputBody);
        } catch (Exception e) {
            responder.setStatusCode("500");
            return false;
        }
        return true;
    }
}

