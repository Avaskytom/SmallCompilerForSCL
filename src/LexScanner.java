/**
*   Program:    LexScanner.java
*   Author:     James Grady | 2/15/2019
*   Abstract:   This class is the implementation of a Scanner that will
*               create tokens for a passed SCL program and place all the
*               necessary information into a List that can later be used
*               by the parser
*/
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class LexScanner {

    // This List will store all the tokens created from the SCL program
    private List<Token> tokens;

    //This is a boolean flag.
    // true - RAISED:  scanner recognizes a multiline comment
    // false - LOWERED: no comment detected or end of comment
    private boolean commentFlag = false;

    // Array of all Keywords found in SCL
    // Removed main from Keywords
    private String[] KEYWORDS = {
            "import", "implementations", "function", "is", "variables", "define", "of", "type",
            "begin", "display", "set", "input", "if", "then", "else", "endif", "not",
            "return", "greater", "or", "equal", "endfun", "pointer", "double"
    };

    /**
     *  CONSTRUCTOR LexScanner
     *  INPUT:      file location in the form of a string
     *  @param filename - must not be null
     *  @throws FileNotFoundException
     *  OUTPUT:     fills the list tokens with the tokens created from filename
     */
    public LexScanner(String filename) throws FileNotFoundException
    {
        assert(filename != null);
        tokens = new ArrayList<>();
        Scanner source = new Scanner(new File(filename));
        int lineNumber = 0;
        while(source.hasNext()){
            String line = source.nextLine();
            processLexemes(line, lineNumber);
            lineNumber++;
        }
        // Denotes the end of the file with a EOF(END OF FILE) Token
        tokens.add(new Token(1,1,"EOF", TokenType.EOF));
        source.close();
    }
    /**
     * FUNCTION processLexemes
     *  @param line - must be greater than or equal to 0
     *  @param lineNumber - must be greater than or equal to 0
     *
     * IMPORTANT INFORMATION:
     *  index:       the beginning spot of the current lexeme (used for column tracking)
     *  lineNumber:  row of the program the lexemes are being created from
     *  tokType:     The type of token each lexeme is classified as. Used for parsing later
     *
     * OUTPUT:
     *  creates lexemes from line and adds all components of that lexeme
     *  into a new token. This token is then added to the list of tokens.
     */
    private void processLexemes(String line, int lineNumber){
        assert(line != null && lineNumber >= 1);
        int index = 0;
        index = skipWhiteSpace(line, index);
        while (index < line.length()){
            String lexeme = getLexeme(line, lineNumber, index);
            if(commentFlag == true || isComment(lexeme) == true)
                break;
            else {
                TokenType tokType = getTokenType(lexeme);
                tokens.add(new Token(lineNumber + 1, index + 1, lexeme, tokType));
                index += lexeme.length();
                index = skipWhiteSpace(line, index);
            }
        }
    }

    /**
     * FUNCTION isComment
     * @param lexeme - cannot be null
     *
     * @return  true - location of code being processed is a comment
     *          false - location of code being processed is not a comment or the end of comment
     */
    private boolean isComment(String lexeme){
        assert(!lexeme.equals(null));
        if(lexeme.equals("*/")|| lexeme.equals("/*") || lexeme.contains("//"))
            return true;
        else
            return false;
    }

    /**
     * FUNCTION: getTokenType
     * @param lexeme - cannot be null
     * @return tokType - dependent of the lexeme being processed | used for parsing later
     */
    private TokenType getTokenType(String lexeme){
        TokenType tokType;
        if(Arrays.asList(KEYWORDS).contains(lexeme)){
            tokType = TokenType.KEYWORD;
        }
        else if(lexeme.contains("\"")){
            tokType = TokenType.STRING_LITERAL;
        }

        else if(lexeme.matches("[0-9]+")){
            tokType = TokenType.INTEGER_CONST;
        }
        else if(lexeme.matches("^\\d*\\.?\\d*$")){
            tokType = TokenType.REAL_CONST;
        }
        else if(lexeme.matches("[+\\-*]")){
            tokType = TokenType.ASSIGNMENT_OPERATOR;
        }
        else if(lexeme.matches("[=]")){ // Added this apart from assignments operators to match BNF
            tokType = TokenType.EQUOP;
        }
        else if(lexeme.matches("[<>=!]=?")){
            tokType = TokenType.RELATIONAL_OPERATOR;
        }
        else if(lexeme.matches("\\p{Punct}")){
            tokType = TokenType.COMMA;
        }
        else
            tokType = TokenType.IDENTIFIER;

        return tokType;
    }

    /**
     * FUNCTION: getLexeme
     * @param line - must not be null | line being currently processed
     * @param lineNumber - must be greater than or equal to 0 | row
     * @param index - must be greater than or equal to 0 | column
     * @return lexeme which is created from the line in the file passed in
     */
    private String getLexeme(String line, int lineNumber, int index){
        assert(line != null && lineNumber >= 1 && index >= 0);
        int i = index;
        char s = line.charAt(i);

        // QUOTES or STRING LITERALS
        if(s == '"'){
            i++;
            s = line.charAt(i);
            while(s != '"'){
                i++;
                s = line.charAt(i);}}

        // COMMMENTS
        else if(s == '/' && line.charAt(i + 1) == '/')
            while(i < line.length())
                i++;

        // LONG COMMENTS
        else if(s == '/' && line.charAt(i + 1) == '*')
            commentFlag = true;
        else if(s == '*' && line.charAt(i + 1) == '/')
            commentFlag = false;

        /*
            ADJUSTMENT: 3/14/2019
         *  Detect commas that were directly at the end of the String Literals.
         *  Added [&& line.charAt(i) != ','] -  This makes sure no comma is at the end of lexeme
         *  Added [if(i == index) i++;] -       This allows you to return 1 character string as lexeme as
         *                                      Substring(i, i) would return NULL lexeme.
         */
        while(i < line.length() && !Character.isWhitespace(line.charAt(i)) && line.charAt(i) != ',')
            i++;
        // Individual Tokens
        if(i == index)
            i++;
        return line.substring(index,i);
    }

    /**
     * FUNCITON: skipWhiteSpace
     * @param line - must not be null | line being currently processed
     * @param index - must be greater than or equal to 0
     * @return the beginning index of the next lexeme
     */
    private int skipWhiteSpace(String line, int index){
        assert(line != null && index >= 0);
        while(index < line.length() && Character.isWhitespace(line.charAt(index)))
            index++;
        return index;
    }

    /**
     * GETTER: getTokens
     * @return tokens
     * This function helps keep the class encapsulated
     */
    public List<Token> getTokens(){
        return tokens;
    }


}
