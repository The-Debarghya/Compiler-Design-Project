package cd_project.parser;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Parser {

    public static void parse(List<String> ids) throws Exception {

            Path fileName = Path.of("./grammar2.txt");
            String str = Files.readString(fileName);
            Grammar g = new Grammar(str);
            LR1Parser lr1parser = new LR1Parser(g);
            Boolean canBeParse = lr1parser.parseCLR1();
//            System.out.println(g.getVariables());
//            System.out.println(g.getTerminals());
//            System.out.println(canBeParse);
            String filename= "./parser.states.txt";
            FileWriter fw1 = new FileWriter(filename,false); //the true will append the new data
            fw1.write(lr1parser.canonicalCollectionStr());//appends the string to the file
            fw1.close();
            String filename2 = "./parser.action.txt";
            FileWriter fw2 = new FileWriter(filename2,false); //the true will append the new data
            fw2.write(lr1parser.actionTableStr());//appends the string to the file
            fw2.close();
            String filename3 = "./parser.goto.txt";
            FileWriter fw3 = new FileWriter(filename3,false); //the true will append the new data
            fw3.write(lr1parser.goToTableStr());//appends the string to the file
            fw3.close();
//             Boolean accept = lr1parser.accept(new ArrayList<String>(Arrays.asList("d", "a")));
            Boolean accept = lr1parser.accept(new ArrayList<String>(ids));
             System.out.println(accept);
    }
}