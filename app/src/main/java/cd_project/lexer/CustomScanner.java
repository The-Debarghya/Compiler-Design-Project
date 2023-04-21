package cd_project.lexer;

import java.io.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class CustomScanner {

    public static String ScanInput(String fileName){
        String fileData = "";
        try {
            File f = new File(fileName);
            Scanner s = new Scanner(f);
            String source = " ";
            while (s.hasNext()) {
                fileData += s.nextLine() + "\n";
            }
            return fileData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileData;
    }
}



