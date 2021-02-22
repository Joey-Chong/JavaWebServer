package Server;

import java.io.File;
import Dictionaries.ConfSettings;


public class FilePathing {

    public static String checkAuthRequired(String path, String previousPath) {
        String[] previousTokens = previousPath.split("/");
        int previousCounter = previousTokens.length - 1;
        String[] tokens = path.split("/");
        String accessFile = ConfSettings.getConfiguration("AccessFile");
        String accessPath = "/" + accessFile;
        if (previousCounter < 0 && checkFileExists(accessPath)) {
            return accessPath;
        }
        String continuedPath = "";
        for (int i = 1; i < tokens.length; i += 1) {
            continuedPath += "/" + tokens[i];
            accessPath = continuedPath + "/" + accessFile;
            if (previousCounter < i && checkFileExists(accessPath)) {
                return accessPath;
            }
        }
        return null;
    }

    public static boolean checkFileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    public static boolean checkFileScriptAliased(String requestPath) {
        String filteredPath = requestPath;
        if (requestPath.contains("://")) {
            filteredPath = createFilteredPath(requestPath, 3);
        } 
        if (handleIsScriptAlias(filteredPath) != null) {
            return true;
        }
        return false;
    }

    public static String handlePathing(String requestPath) {
        if (requestPath.contains("://")) {
            String filteredPath = createFilteredPath(requestPath, 3);
            return rootPathing(filteredPath);
        } 
        return rootPathing(requestPath);
    }

    private static String createFilteredPath(String requestPath, int startIndex) {
        String[] tokens = requestPath.split("/");
        String filteredPath = "";
        if (tokens.length == startIndex) {
            filteredPath += "/";
        } else {
            for (int i = startIndex; i < tokens.length; i++) {
                filteredPath += "/" + tokens[i];
            }
        }
        return filteredPath;
    }

    private static String rootPathing(String requestPath) {
        String serverPath = "";
        if ((serverPath = handleIsAlias(requestPath)) == null) {
            if ((serverPath = handleIsScriptAlias(requestPath)) == null) {
                serverPath = assignToRoot(requestPath);
            }
        }
        return handleIsFile(serverPath);
    }

    private static String handleIsAlias(String requestPath) {
        String aliasPath = "";
        String[] tokens = requestPath.split("/");
        for (int i = 1; i < tokens.length; i += 1) {
            String tokenPath = "/" + tokens[i] + "/";
            if ((aliasPath = ConfSettings.getScriptAlias(tokenPath)) != null) {
                for (int j = i + 1; j < tokens.length; j += 1) {
                    aliasPath += "/" + tokens[j];
                    return aliasPath;
                }
            }
        }
        return ConfSettings.getAlias(requestPath);
    }

    private static String handleIsScriptAlias(String requestPath) {
        String scriptPath = "";
        String[] tokens = requestPath.split("/");
        for (int i = 1; i < tokens.length; i += 1) {
            String tokenPath = "/" + tokens[i] + "/";
            if ((scriptPath = ConfSettings.getScriptAlias(tokenPath)) != null) {
                for (int j = i + 1; j < tokens.length; j += 1) {
                    scriptPath += "/" + tokens[j];
                    return scriptPath;
                }
            }
        }
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
            completePath += "/" + ConfSettings.getDirectoryIndex();
        }
        return completePath;
    }
}