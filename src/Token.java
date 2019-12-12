
enum TokenType {

    IDENTIFIER("IDENTIFIER"), KEYWORD("KEYWORD"), STRING_LITERAL("STRING LITERAL"),
    RELATIONAL_OPERATOR("RELATIONAL OPERATOR"), ASSIGNMENT_OPERATOR("ASSIGNMENT OPERATOR"), REAL_CONST("REAL CONST"),
    INTEGER_CONST("INTEGER CONST"), EOF("EOF"), COMMA("COMMA"), EQUOP("="); // ADDED Comma Token Type

    private String custom;
    private TokenType(String custom) {
        this.custom = custom;
    }
    public String getCustomString() {
        return custom;
    }
}
public class Token {

    private int rowNumber;
    private int columnNumber;
    private String lexeme;
    private TokenType tokType;

    /**
     * CONSTRUCTOR: Token
     * @param rowNumber - must be greater than or equal to 0
     * @param columnNumber - must be greater than or equal to 0
     * @param lexeme - must not be null
     * @param tokType - must not be null
     * @throws IllegalArgumentException - if any state doesnt meat the proper requirements
     */
    public Token(int rowNumber, int columnNumber, String lexeme, TokenType tokType){
        if(rowNumber <= 0)
            throw new IllegalArgumentException("Invalid Row");
        if(columnNumber <= 0)
            throw new IllegalArgumentException("Invalid Column");
        if(lexeme == null  || lexeme.length() == 0)
            throw new IllegalArgumentException("Invalid Lexeme");
        if(tokType == null)
            throw new IllegalArgumentException("Invalid Token Type");
        this.rowNumber = rowNumber;
        this.columnNumber = columnNumber;
        this.lexeme = lexeme;
        this.tokType = tokType;
    }

    /**
     * GETTER: getRowNumber
     * @return row number
     * keeps class encapsulation
     */
    public int getRowNumber(){
        return rowNumber;
    }

    /**
     * GETTER: getColumnNumber
     * @return column number
     * keeps class encapsulation
     */
    public int getColumnNumber(){
        return columnNumber;
    }

    /**
     * GETTER: getLexeme
     * @return lexeme
     * keeps class encapsulation
     */
    public String getLexeme(){
        return lexeme;
    }

    /**
     * GETTER: getTokType
     * @return tokType
     * keeps class encapsulation
     */
    public TokenType getTokType(){
        return tokType;
    }

}
