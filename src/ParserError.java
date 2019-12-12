public class ParserError extends Error {

    public ParserError(Token token, TokenType expected){
        super("Error at Line " + token.getRowNumber() + ": Expected "
                + expected.getCustomString() + " Got " + token.getLexeme());
    }
    public ParserError(Token token, String message){
        super("Error at Line " + token.getRowNumber() + ": Expected \""
                + message + "\"");
    }
    public ParserError(Token token, Terminals expected){
        super("Error at Line " + token.getRowNumber() + ": Expected "
                + expected.getCustomString() + " Got " + token.getLexeme());
    }

}
