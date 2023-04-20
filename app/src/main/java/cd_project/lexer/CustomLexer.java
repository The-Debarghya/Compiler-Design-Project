package cd_project.lexer;


import java.util.HashMap;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class CustomLexer {
    public enum Tokens{
        INT,
        STRING,
        FLOAT,
        FOR,
        CONTINUE,
        BREAK,
        VOID,
        RETURN,
        MAIN,
        ADDOP,
        DIFOP,
        RELOP,
        LPAREN,
        RPAREN,
        LBRACE,
        RBRACE,
        SEMI,
        COMMA,
        ASSIGN,
        ID,
        ICONST,
        FCONST,
        SCONST,
        NEWLINE,
        SPACE,
        ERROR
    }
    public TreeMap<Tokens, Pattern> tokensToRegex;
    public CustomLexer(){
        // default
//        System.out.println("default");
        // declaration
        tokensToRegex = new TreeMap<>();
        // initialization
        tokensToRegex.put(Tokens.INT, Pattern.compile("int"));
        tokensToRegex.put(Tokens.FLOAT, Pattern.compile("float"));
        tokensToRegex.put(Tokens.RETURN, Pattern.compile("return"));
        tokensToRegex.put(Tokens.STRING, Pattern.compile("string"));
        tokensToRegex.put(Tokens.FOR, Pattern.compile("for"));
        tokensToRegex.put(Tokens.CONTINUE, Pattern.compile("continue"));
        tokensToRegex.put(Tokens.BREAK, Pattern.compile("break"));
        tokensToRegex.put(Tokens.VOID, Pattern.compile("void"));
        tokensToRegex.put(Tokens.MAIN, Pattern.compile("main"));
        tokensToRegex.put(Tokens.ADDOP, Pattern.compile("\\+"));
        tokensToRegex.put(Tokens.DIFOP, Pattern.compile("-"));
        tokensToRegex.put(Tokens.RELOP, Pattern.compile("(\\.gt\\.)|(\\.lt\\.)|(\\.le\\.)|(\\.ge\\.)"));
        tokensToRegex.put(Tokens.LPAREN, Pattern.compile("\\("));
        tokensToRegex.put(Tokens.RPAREN, Pattern.compile("\\)"));
        tokensToRegex.put(Tokens.LBRACE, Pattern.compile("\\{"));
        tokensToRegex.put(Tokens.RBRACE, Pattern.compile("\\}"));
        tokensToRegex.put(Tokens.SEMI, Pattern.compile(";"));
        tokensToRegex.put(Tokens.COMMA, Pattern.compile(","));
        tokensToRegex.put(Tokens.ASSIGN, Pattern.compile("="));
        tokensToRegex.put(Tokens.ICONST, Pattern.compile("'0'|[0-9]+"));
        tokensToRegex.put(Tokens.FCONST, Pattern.compile("'0'|[0-9]*\\.[0-9]+"));
        tokensToRegex.put(Tokens.SCONST, Pattern.compile("\"[ -~]*\""));
        tokensToRegex.put(Tokens.ID, Pattern.compile("[a-zA-Z_]+[a-zA-Z0-9_]*"));
        tokensToRegex.put(Tokens.NEWLINE, Pattern.compile("\\n"));
        tokensToRegex.put(Tokens.SPACE, Pattern.compile("[\s\t]+"));
        tokensToRegex.put(Tokens.ERROR, Pattern.compile(".+"));
    }
}
