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
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class CGIScript {

    public static boolean executeScript(String scriptPath, String queryString, ParseHttpRequest parser) {
        try {
            ProcessBuilder operatingBuilder = new ProcessBuilder(scriptPath);
            Map<String, String> environmentMap = operatingBuilder.environment();
            if (!assignEnvironmentVariables(environmentMap, queryString, parser.getHeaderMap())) {
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

            if (!assignInput(operation, parser)) {
                parser.getResponder().setStatusCode("500");
                return false;
            }
            if (!redirectOutput(operation, parser.getResponder())) {
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

    private static boolean assignEnvironmentVariables(Map<String, String> environmentMap, String queryString, HashMap<String, String> headerMap) {
        try {
            environmentMap.put("QUERY_STRING", queryString);
            environmentMap.put("SERVER_PROTOCOL", ResponseDictionary.getSupportedVersion());
            Iterator headerIterator = headerMap.entrySet().iterator(); 
            while (headerIterator.hasNext()) { 
                Map.Entry mapElement = (Map.Entry)headerIterator.next(); 
                String key = "HTTP_" + ((String)mapElement.getKey()).toUpperCase();
                environmentMap.put(key, (String)mapElement.getValue());
            } 
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private static boolean assignInput(Process operation, ParseHttpRequest parser) {
        try {
            BufferedWriter redirectWriter = new BufferedWriter(new OutputStreamWriter(operation.getOutputStream()));
            redirectWriter.write(parser.getBody());
            redirectWriter.flush();
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

