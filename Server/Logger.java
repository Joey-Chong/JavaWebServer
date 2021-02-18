package Server;

import Dictionaries.ConfSettings;

import java.io.*;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Logger {
    private String remoteAddress;
    //hyphen skipped
    private String userName;
    private SimpleDateFormat date;
    private String dateString;
    private String methodLine;
    private String statusCode;
    private String responseBodyLength;
    private ParseHttpRequest requestParser;

    private ArrayList<String> path;
    File file;
    FileWriter writer;
    BufferedWriter bw;
    PrintWriter out;

    public Logger(ParseHttpRequest requestParser) {
        remoteAddress = requestParser.getRemoteAddress();
        //user name
        date = new SimpleDateFormat("dd/MMM/yyyy:hh:mm:ss Z");
        dateString = date.format(new Date());
        methodLine = requestParser.getMethodLine();
        statusCode = requestParser.getStatusCode();
        //get response body length

        path = new ArrayList<>();
//        placeholder path
//        path.add("C:\\Users\\joey_\\IdeaProjects\\web-server-joey-adam-team\\");
        path = ConfSettings.getValue("LogFile");    //real path
        String fileName = path.get(0) + "log.txt";

        file = new File(fileName);
        if(file.exists()) {
            try {
                writeLogLine();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                file.createNewFile();
                writeLogLine();
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    private void writeLogLine() throws IOException {
        System.out.println("------- Logging -------");
        writer = new FileWriter(file, true);
        bw = new BufferedWriter(writer);
        out = new PrintWriter(bw);

        String logLine = String.format("%s %s \"%s\" %s", remoteAddress.toString(),dateString.toString(),
                methodLine, statusCode);
        out.println(logLine);
        System.out.println(logLine);
        out.close();
        System.out.println("------- Logging Done -------");
    }

    public static void main(String[] args) {
        SimpleDateFormat date = new SimpleDateFormat("[dd/MMM/yyyy:hh:mm:ss Z]");
        String dateString = date.format(new Date());
        System.out.println(dateString);
    }
}
