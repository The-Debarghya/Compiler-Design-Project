package cd_project.lexer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.List;

public class CustomScanner {

    public static List<String> ScanInput(){
        List<String> fileData = new ArrayList<String>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("/Users/rohit/Desktop/Assignments/6th Semester/Compiler Design Lab/project/revamped/app/src/main/java/cd_project/input.c"));
            String line = reader.readLine();
            while (line != null) {
                fileData.add(line);
                line = reader.readLine();
            }
            reader.close();
            return fileData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileData;
    }
}



