package Server.HttpMethods;

import java.util.HashMap;

public class MethodTable {
    private static HashMap<String, String> methodTable;

    public static void init () {
        methodTable = new HashMap<String, String>();
        methodTable.put("GET", "GetMethod");
        methodTable.put("HEAD", "HeadMethod");
        methodTable.put("POST", "PostMethod");
        methodTable.put("PUT", "PutMethod");
        methodTable.put("DELETE", "DeleteMethod");
    }

    public static String get(String code) {
        return methodTable.get(code);
    }
}
