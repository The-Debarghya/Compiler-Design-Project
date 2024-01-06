package cd_project.lexer;

import java.util.HashMap;
import java.util.*;

public class CustomLexer {
    private int line;
    private int pos;
    private int position;
    private char chr;
    private String s;

    Map<String, TokenType> keywords = new HashMap<>();

    static class Token {
        public TokenType tokentype;
        public String value;
        public int line;
        public int pos;

        Token(TokenType token, String value, int line, int pos) {
            this.tokentype = token;
            this.value = value;
            this.line = line;
            this.pos = pos;
        }

        @Override
        public String toString() {
            String result = String.format("%5d  %5d %-15s", this.line, this.pos, this.tokentype);
            switch (this.tokentype) {
                case IntegerConst:
                    result += String.format("  %4s", value);
                    break;
                case Identifier:
                    result += String.format(" %s", value);
                    break;
                case StringConst:
                    result += String.format(" \"%s\"", value);
                    break;
                case FloatConst:
                    result += String.format(" %s", value);
                    break;
                default:
                    break;
            }
            return result;
        }
    }

    public static enum TokenType {
        // constants
        IntegerConst, FloatConst, StringConst,

        // keywords
        INT, STRING, FLOAT, FOR, CONTINUE, BREAK, VOID, RETURN, MAIN,

        // identifier
        Identifier,

        // operators
        GREATERTHAN, LESSTHAN, GREATEREQUALS, LESSEQUALS,
        ADDOP, DIFOP, LPAREN, RPAREN, LBRACE, RBRACE, SEMI,
        COMMA, ASSIGN,

        // others
        EQUAL, EOI

    }

    public static void error(int line, int pos, String msg) {
        if (line > 0 && pos > 0) {
            System.out.printf("%s in line %d, pos %d\n", msg, line, pos);
        } else {
            System.out.println(msg);
        }
        System.exit(1);
    }

    public CustomLexer(String source) {
        this.line = 1;
        this.pos = 0;
        this.position = 0;
        this.s = source;
        this.chr = this.s.charAt(0);
        this.keywords.put("int", TokenType.INT);
        this.keywords.put("string", TokenType.STRING);
        this.keywords.put("float", TokenType.FLOAT);
        this.keywords.put("continue", TokenType.CONTINUE);
        this.keywords.put("break", TokenType.BREAK);
        this.keywords.put("void", TokenType.VOID);
        this.keywords.put("return", TokenType.RETURN);
        this.keywords.put("main", TokenType.MAIN);
        this.keywords.put("for", TokenType.FOR);
    }

    Token follow(char expect, TokenType ifyes, TokenType ifno, int line, int pos) {
        if (getNextChar() == expect) {
            getNextChar();
            return new Token(ifyes, "", line, pos);
        }
        if (ifno == TokenType.EOI) {
            error(line, pos, String.format("follow: unrecognized character: (%d) '%c'", (int) this.chr, this.chr));
        }
        return new Token(ifno, "", line, pos);
    }

    Token char_lit(int line, int pos) {
        char c = getNextChar(); // skip opening quote
        int n = (int) c;
        if (c == '\'') {
            error(line, pos, "empty character constant");
        } else if (c == '\\') {
            c = getNextChar();
            if (c == 'n') {
                n = 10;
            } else if (c == '\\') {
                n = '\\';
            } else {
                error(line, pos, String.format("unknown escape sequence \\%c", c));
            }
        }
        if (getNextChar() != '\'') {
            error(line, pos, "multi-character constant");
        }
        getNextChar();
        return new Token(TokenType.IntegerConst, "" + n, line, pos);
    }

    Token string_lit(char start, int line, int pos) {
        String result = "";
        while (getNextChar() != start) {
            if (this.chr == '\u0000') {
                error(line, pos, "EOF while scanning string literal");
            }
            if (this.chr == '\n') {
                error(line, pos, "EOL while scanning string literal");
            }
            result += this.chr;
        }
        getNextChar();
        return new Token(TokenType.StringConst, result, line, pos);
    }

    Token identifier_or_integer(int line, int pos) {

        String text = "";

        // identifier or integer
        while (Character.isAlphabetic(this.chr) || Character.isDigit(this.chr) || this.chr == '_' || this.chr == '.') {
            text += this.chr;
            getNextChar();
        }
        // System.out.println(text);

        if (text.equals("")) {
            error(line, pos,
                    String.format("identifer_or_integer unrecognized character: (%d) %c", (int) this.chr, this.chr));
        }

        int ind = text.indexOf('.');
        if (Character.isDigit(text.charAt(0))) {
            try {

                if (ind != -1) {
                    String str[] = text.split("\\.");
                    List<String> l = new ArrayList<String>();
                    l = Arrays.asList(str);
                    // System.out.println(l);
                    if (l.size() != 2)
                        error(line, pos, String.format("invalid number: %s", text));
                }
                Float.parseFloat(text);
                try {
                    Integer.parseInt(text);
                    return new Token(TokenType.IntegerConst, text, line, pos);
                } catch (Exception e) {
                    return new Token(TokenType.FloatConst, text, line, pos);
                }
            } catch (Exception e) {
                error(line, pos, String.format("invalid number: %s", text));
            }
        }

        if (this.keywords.containsKey(text)) {
            return new Token(this.keywords.get(text), "", line, pos);
        }
        // check for identifier
        if (ind != -1) {
            error(line, pos, String.format("invalid identifier: %s", text));
        }
        return new Token(TokenType.Identifier, text, line, pos);
    }

    Token getToken() {
        int line, pos;
        while (Character.isWhitespace(this.chr)) {
            getNextChar();
        }
        line = this.line;
        pos = this.pos;

        switch (this.chr) {
            case '\u0000':
                return new Token(TokenType.EOI, "", this.line, this.pos);
            // case '/': return div_or_comment(line, pos);
            case '\'':
                return char_lit(line, pos);
            // case '<': return follow('=', TokenType.Op_lessequal, TokenType.Op_less, line,
            // pos);
            // case '>': return follow('=', TokenType.Op_greaterequal, TokenType.Op_greater,
            // line, pos);
            case '=':
                return follow('=', TokenType.EQUAL, TokenType.ASSIGN, line, pos);
            // case '!': return follow('=', TokenType.Op_notequal, TokenType.Op_not, line,
            // pos);
            // case '&': return follow('&', TokenType.Op_and, TokenType.End_of_input, line,
            // pos);
            // case '|': return follow('|', TokenType.Op_or, TokenType.End_of_input, line,
            // pos);
            case '"':
                return string_lit(this.chr, line, pos);
            case '{':
                getNextChar();
                return new Token(TokenType.LBRACE, "", line, pos);
            case '}':
                getNextChar();
                return new Token(TokenType.RBRACE, "", line, pos);
            case '(':
                getNextChar();
                return new Token(TokenType.LPAREN, "", line, pos);
            case ')':
                getNextChar();
                return new Token(TokenType.RPAREN, "", line, pos);
            case '+':
                getNextChar();
                return new Token(TokenType.ADDOP, "", line, pos);
            case '-':
                getNextChar();
                return new Token(TokenType.DIFOP, "", line, pos);
            // case '*': getNextChar(); return new Token(TokenType.Op_multiply, "", line,
            // pos);
            // case '%': getNextChar(); return new Token(TokenType.Op_mod, "", line, pos);
            case ';':
                getNextChar();
                return new Token(TokenType.SEMI, "", line, pos);
            case ',':
                getNextChar();
                return new Token(TokenType.COMMA, "", line, pos);
            case '.': {
                getNextChar();
                // .gt. .lt.
                if (this.chr == 'g') {
                    getNextChar();
                    char next = this.chr;
                    getNextChar();
                    char ending = this.chr;
                    if (next == 't' && ending == '.') {
                        getNextChar();
                        return new Token(TokenType.GREATERTHAN, "", line, pos);
                    } else if (next == 'e' && ending == '.') {
                        getNextChar();
                        return new Token(TokenType.GREATEREQUALS, "", line, pos);
                    } else {
                        error(line, pos, String.format("invalid . : %s", "."));
                    }
                } else if (this.chr == 'l') {
                    getNextChar();
                    char next = this.chr;
                    getNextChar();
                    char ending = this.chr;
                    if (next == 't' && ending == '.') {
                        getNextChar();
                        return new Token(TokenType.LESSTHAN, "", line, pos);
                    } else if (next == 'e' && ending == '.') {
                        getNextChar();
                        return new Token(TokenType.LESSEQUALS, "", line, pos);
                    } else {
                        error(line, pos, String.format("invalid . : %s", "."));
                    }
                } else {
                    error(line, pos, String.format("invalid . : %s", "."));
                }
            }
            default:
                return identifier_or_integer(line, pos);
        }
    }

    char getNextChar() {
        this.pos++;
        this.position++;
        if (this.position >= this.s.length()) {
            this.chr = '\u0000';
            return this.chr;
        }
        this.chr = this.s.charAt(this.position);
        if (this.chr == '\n') {
            this.line++;
            this.pos = 0;
        }
        return this.chr;
    }

    public void printTokens(ArrayList<TokenType> a, HashMap<String, List<List<Integer>>> symbolTable) {
        Token t;
        int line = 1;
        while ((t = getToken()).tokentype != TokenType.EOI) {
            if (t.line != line) {
                line++;
                System.out.println("------------------------------------------");
            }
            System.out.println(t);
            a.add(t.tokentype);
            // checking for tokens and adding to symbol table
            if (t.tokentype == TokenType.Identifier) {
                // if already exists
                List<List<Integer>> list = symbolTable.get(t.value);
                if (list != null && list.size() > 0) {
                    List<Integer> l = new ArrayList<Integer>();
                    l.add(t.line);
                    l.add(t.pos);
                    list.add(l);
                    symbolTable.put(t.value, list);
                } else {
                    list = new ArrayList<List<Integer>>();
                    List<Integer> l = new ArrayList<Integer>();
                    l.add(t.line);
                    l.add(t.pos);
                    list.add(l);
                    symbolTable.put(t.value, list);
                }
            }
        }
        System.out.println(t);
    }
}