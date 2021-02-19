package Server;

import java.io.File;
import Dictionaries.ConfSettings;


public class FilePathing {

    public static String filterURL(String requestPath) {
        if (requestPath.contains("://")) {
            String[] tokens = requestPath.split("/");
            String filteredPath = "";
            for (int i = 2; i < tokens.length; i++) {
                filteredPath += "/" + tokens[i];
            }
            return filteredPath;
        }
        return requestPath;
    }

    public static String handlePathing(String requestPath, String requestFile) {
        String serverPath = "";
        if ((serverPath = handleIsAlias(requestPath)) == NULL) {
            if ((serverPath = handleIsScriptAlias(requestPath)) == NULL) {
                serverPath = assignToRoot(requestPath);
            }
        }
        serverPath += requestFile
        return handleIsFile(serverPath);
    }

    public static boolean checkAuthRequired(String path) {
        return configurationsDictionary.get(option);
    }

    public static boolean checkFileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    private static String handleIsAlias(String requestPath) {
        return ConfSettings.getAlias(requestPath);
    }

    private static String handleIsScriptAlias(String requestPath) {
        return ConfSettings.getScriptAlias(requestPath);
    }

    private static String assignToRoot(String requestPath) {
        String rootPath = ConfSettings.getConfiguration("DocumentRoot") + requestPath;
        return rootPath;
    }

    private static String handleIsFile(String filePath) {
        File file = new File(filePath);
        String completePath = filePath;
        if (file.isDirectory()) {
            completePath += ConfSettings.getConfiguration("DocumentRoot");
        }
        return completePath;
    }
}