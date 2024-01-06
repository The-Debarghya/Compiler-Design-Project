package cd_project.lexer;

import java.io.*;
import java.util.*;

public class CustomScanner {

    public static String ScanInput(String fileName){
        String fileData = "";
        try {
            File f = new File(fileName);
            Scanner s = new Scanner(f);
            while (s.hasNext()) {
                fileData += s.nextLine() + "\n";
            }
            s.close();
            return fileData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileData;
    }
}



