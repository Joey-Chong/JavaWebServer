package Server.HttpMethods;

import Server.FilePathing;
import Server.HttpResponse;
import Server.ParseHttpRequest;
import Dictionaries.MimeSettings;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GetMethod extends HttpMethod{
    private String filePath;
    private File file;
    private PrintWriter out;

    @Override
    public void execute(String filePath, HttpResponse response) {
        System.out.println("Creating instance GET");
        //filePath = "C:\\Users\\joey_\\IdeaProjects\\web-server-joey-adam-team\\public_html\\index.html";

        file = new File(filePath);
        response.setContentLength(String.valueOf(file.length()));
        //String fileType = MimeSettings.getType(FilenameUtils.getExtension(filePath));
        //response.setContentType(fileType);

        /**out = response.getResponseWriter(file.length());

        out.print("HTTP/1.1 200 OK\r\n");
        out.print("Content-Type: text/html\r\n");
//        out.print("Content-Length: " + file.length() + "\r\n");
        out.print("Content-Length: 3209" + "\r\n");
        out.print("Access-Control-Allow-Origin: *\r\n");
        out.print("Access-Control-Allow-Methods: *\r\n");
        out.print("Access-Control-Allow-Headers: *\r\n");
        out.print("\r\n");
        out.flush();**/

        try {
            String body = "";
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = "";
            while ((line = reader.readLine()) != null) {
                body += line + "\n";
            }
            response.setResponseBody(body);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        try {
//            byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
//            out.print(fileBytes);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public String getStatusCode() {
        return null;
    }
}