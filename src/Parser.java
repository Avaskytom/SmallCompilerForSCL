
/**
 *   Program:    LexScanner.java
 *   Author:     James Grady | 3/14/2019
 *   Abstract:   This class is the implementation of a Scanner that will
 *               create tokens for a passed SCL program and place all the
 *               necessary information into a List that can later be used
 *               by the parser
 */

import java.util.ArrayList;
import java.util.List;


enum Terminals {

    IMPLEMENTATIONS("implementations"), FUNCTION("function"), IS("is"), BEGIN("begin"),
    ENDFUN("endfun"), VARIABLES("variables"), DEFINE("define"), OF("of"), TYPE("type");

    private String custom;
    private Terminals(String custom) {
        this.custom = custom;
    }
    public String getCustomString() {
        return custom;
    }
}

public class Parser {

    // This List will store all the token statements created from the SCL program
    private List<Statement> tokenStatements = new ArrayList<>();
    // List for all tokens from the scanner
    private List<Token> tokens;
    // Statement instance declaration
    private Statement statement = new Statement();

    // Token holders
    private Token currentToken;
    private Token nextToken;

    // Indexers
    private int index;
    private int rowIndex;


    public Parser (List<Token> tokens) throws ParserError{
        // Initialize Store list
        this.tokens = tokens;
        // Initialize Indexes
        index = 0;
        rowIndex = this.tokens.get(index).getRowNumber();
        // Initialize Token place holders
        currentToken = this.tokens.get(index);
        nextToken = this.tokens.get(index+1);

        // Runs the Program and will process with exit code 0 if all correct
        if(implement() && nextToken.getTokType()==TokenType.EOF){
            storeToken(nextToken);
        }

    }
    /**
     * @return Token Statements array
     */
    public List<Statement> getTokenStatements(){
        return tokenStatements;
    }
    /**
     * Starting function for the BNF
     * BNF -> IMPLEMENTATIONS func_body
     * @return true if the entire program is parsed without errors.
     * @throws ParserError if starting token is not IMPLEMENTATIONS
     */
    private boolean implement(){
        if(currentToken.getLexeme().equals(Terminals.IMPLEMENTATIONS.getCustomString())){
            storeToken(currentToken);
            return funcBody();
        }else
            throw new ParserError(currentToken, Terminals.IMPLEMENTATIONS);
    }
    /**
     * Function header
     * BNF -> FUNCTION pother_oper_def
     * @return true if BNF is returned correct
     * @throws ParserError if starting token is not FUNCTION
     */
    private boolean funcBody(){
        incrementToken();
        if(currentToken.getLexeme().equals(Terminals.FUNCTION.getCustomString())){
            storeToken(currentToken);
            return potherOperDef();
        }
        else
            throw new ParserError(currentToken, Terminals.FUNCTION);
    }
    /**
     * This is one of the main functions of the program
     * as This entire statement makes up most of the code
     * BNF -> pother_oper IS const_var_struct BEGIN pactions ENDFUN IDENTIFIER
     * @return true if all of the BNF statements returned are true otherwise ERRORS are returned
     */
    private boolean potherOperDef(){
        incrementToken(); // Token now MAIN
        if(potherOper() && nextToken.getLexeme().equals(Terminals.IS.getCustomString())){
            incrementToken();
            storeToken(currentToken);
            incrementToken();
            if(constVarStruct() && nextToken.getLexeme().equals(Terminals.BEGIN.getCustomString())){
                if(pactions() && nextToken.getLexeme().equals(Terminals.ENDFUN.getCustomString())){
                    incrementToken();
                    storeToken(currentToken);
                    incrementToken();
                    return potherOper();}
            }else
                throw new ParserError(nextToken, Terminals.BEGIN);
            return true;}
        else
            throw new ParserError(currentToken, Terminals.IS);
    }
    /**
     * will recursively run and check actionDef until there is no more code
     * or until the ENDFUN terminal token is located.
     * @return true if pactions are correct or if ENDFUN is located.
     * @throws IndexOutOfBoundsException if ENDFUN terminal token not located
     */
    private boolean pactions(){
        if(nextToken.getLexeme().equals(Terminals.ENDFUN.getCustomString())){
            return true;
        }else
            actionDef();
        return pactions();
    }
    /**
     * Checks if the statement header begins with
     * DISPLAY, SET, INPUT, or RETURN
     * @return true if the statement is correct according to BNF rules
     * @throws ParserError if EQUOP token not located in set function
     */
    private boolean actionDef(){
        storeToken(currentToken);
        incrementToken();
        switch (currentToken.getLexeme()){
            case "display":
                storeToken(currentToken);
                incrementToken();
                return pvarValueList();
            case "set":
                storeToken(currentToken);
                incrementToken();
                if(nameRef() && nextToken.getTokType() == TokenType.EQUOP){
                    incrementToken();
                    storeToken(currentToken);
                    incrementToken();
                    return expr();
                }else
                    throw new ParserError(nextToken, TokenType.EQUOP);
            case "input":
                storeToken(currentToken);
                incrementToken();
                return nameRef();
            case "return":
                storeToken(currentToken);
                incrementToken();
                return expr();
            default:
                return false;
        }
    }
    /**
     * checks if the nameRef is either a identifier
     * or checks optRef
     * @return true if statement follows recursive BNF
     */
    private boolean nameRef(){
        if (isIdentifier()){
            storeToken(currentToken);
            return true;
        }else
            return optRef();
    }
    private boolean optRef(){
       return arrayVal();
    } /** Simple return to follow BNF more accurately **/
    private boolean arrayVal(){
        return simpArrValue();
    } /** Simple return to follow BNF more accurately **/
    private boolean simpArrValue(){
       return argList();
    }/** Simple return to follow BNF more accurately **/
    /**
     * Tests and runs if the expression is just
     * EXPR or if its ARG_LIST COMMA EXPR
     * @return true if either of the statement is true
     */
    private boolean argList(){
        if(nextToken.getTokType() == TokenType.COMMA){
            expr();
            storeToken(currentToken);
            incrementToken();
            storeToken(currentToken);
            incrementToken();
            return expr();
        }

        return expr();
    }
    /**
     * Tests and runs if the expression is just
     * EXPR or if its ARG_LIST COMMA EXPR
     * @return true if either of the statement is true
     */
    private boolean pvarValueList(){
        if(nextToken.getTokType() == TokenType.COMMA){
            expr();
            storeToken(currentToken);
            incrementToken();
            storeToken(currentToken);
            incrementToken();
            return expr();
        }

        return expr();
    }
    private boolean expr(){
        return term();
    }/** Simple return to follow BNF more accurately **/
    private boolean term(){
        return punary();
    }/** Simple return to follow BNF more accurately **/
    private boolean punary(){
       return element();
    }/** Simple return to follow BNF more accurately **/
    /**
     * Tests is the element is either a
     * STRING LITERAL, REAL CONST, INTEGER CONST, or INDENTIFIER
     * @return true if element is found
     * @throws ParserError if not one of the needed Token Types
     */
    private boolean element(){
        switch (currentToken.getTokType()){
            case STRING_LITERAL:
                storeToken(currentToken); // Store  "Welcome to the world of SCL"
                return true;
            case REAL_CONST:
                storeToken(currentToken);
                return true;
            case INTEGER_CONST:
                storeToken(currentToken);
                return true;
            case IDENTIFIER:
                storeToken(currentToken);
                return true;
            default:
                throw new ParserError(currentToken, "STRING LITERAL, REAL CONST, INTEGER CONST, or IDENTIFIER");
        }
    }
    /**
     * @return if token is IDENTIFIER
     * @throws ParserError if token is not
     */
    private boolean potherOper(){
        if(isIdentifier()){
            storeToken(currentToken);
            return true;
        }else
            throw new ParserError(currentToken, TokenType.IDENTIFIER);
    }
    /**
     * @return if the current token is IDENTIFIER
     */
    private boolean isIdentifier(){
        return currentToken.getTokType() == TokenType.IDENTIFIER;
    }
    private boolean constVarStruct(){
        return varDec();
    } /** Simple return to follow BNF more accurately **/
    /**
     * Variables declaration header method
     * @return True if header and statements are correct according to BNF
     * @throws ParserError if Header is not correct
     */
    private boolean varDec(){
        if(currentToken.getLexeme().equals(Terminals.VARIABLES.getCustomString())){
            storeToken(currentToken); // Store VARIABLES
            return dataDeclarations();
        }else
            throw new ParserError(currentToken, Terminals.VARIABLES);
    }
    /**
     * will recursively continue to search for dataDeclarations
     * until the BEGIN terminal is the next token as per the BNF
     * @return true if correct statement is found
     */
    private boolean dataDeclarations(){
        if (nextToken.getLexeme().equals(Terminals.BEGIN.getCustomString())){
            return true;
        }
        compDeclare();
        return dataDeclarations();
    }
    /**
     * checks if the next statement is:
     * DEFINE datafile()
     *
     * @return true if the full statement is correct
     * @throws ParserError if declaration doesnt start with define
     */
    private boolean compDeclare(){
        incrementToken();
        if(currentToken.getLexeme().equals(Terminals.DEFINE.getCustomString())){
            storeToken(currentToken);
            return dataFile();
        }else
            throw new ParserError(currentToken, Terminals.DEFINE);
    }
    private boolean dataFile(){
        return dataDeclare();
    } /** Simple return to follow BNF more accurately **/
    /**
     * this confirms the statement:
     * IDENTIFIER OF TYPE DATATYPE
     * is syntatically correct.
     * @return true if the statement is true
     * @throws ParserError if OF TYPE not correct
     * @throws ParserError if first token is not a IDENTIFIER
     */
    private boolean dataDeclare(){
        incrementToken();
        if (isIdentifier()){
            storeToken(currentToken); // Store X
            incrementToken(); // Token now OF
            if(currentToken.getLexeme().equals(Terminals.OF.getCustomString()) && nextToken.getLexeme().equals(Terminals.TYPE.getCustomString())){
                storeToken(currentToken);
                storeToken(nextToken);
                incrementToken();
                incrementToken();
                return dataType();
            } else
                throw new ParserError(currentToken, "OF TYPE");}
        else
            throw new ParserError(currentToken, TokenType.IDENTIFIER);
    }
    /**
     * Checks token to be either
     * DOUBLE INTEGER REAL or LONG
     *
     * @return true if the datatype is confirmed
     * @throws ParserError if token is not correct type
     */
    private boolean dataType(){
        switch (currentToken.getLexeme()){
            case "double":
                storeToken(currentToken); // Store DOUBLE
                return true;
            case "integer":
                storeToken(currentToken); // Store INTEGER
                return true;
            case "real":
                storeToken(currentToken); // Store REAL
                return true;
            case "long":
                storeToken(currentToken); // Store LONG
                return true;
            default:
                throw new ParserError(currentToken, "DOUBLE, INTEGER, REAL, or LONG");
        }
    }
    /**
     * Tests weather the statement is on the same line
     * since in SCL the statements are broken up into lines
     *
     * This stores the tokens recognized in the appropriate statement
     * @param token must not be NULL
     */
    private void storeToken(Token token){

        int tokenRow = token.getRowNumber();
        if(rowIndex == tokenRow){
            statement.createStatement(token);
        }else {
            rowIndex = tokenRow;
            tokenStatements.add(statement);
            statement = new Statement();
            statement.createStatement(token);
        }
    }
    /**
     * Increment the token to the next token
     * in the Token List passed in by the Scanner
     */
    private void incrementToken(){
        index++;
        currentToken = tokens.get(index);
        nextToken = tokens.get(index+1);
    }

}
