package Server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.util.HashMap;

public class ContentTypeInit {
    public ContentTypeInit() throws IOException {
        init();
    }

    private void init() throws IOException {
        HashMap<String, String> contentType = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader("conf/mime.types"));

        String line = "";
        while ((line = reader.readLine()) != null) {
            if(!line.isEmpty()) {
                //excluding comments
                if ((line.charAt(0) != '#')) {
//                    System.out.println(line);
                }
            }
        }

    }

}