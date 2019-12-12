/**
 * Program:     Statement.java
 * Author:      James Grady | 3/18/2019
 * Abstract:    This class is an implementation of a statement that
 *              will take tokens of a statement and form them into
 *              a string form and also a Token List so that the statement
 *              can later be accessed for all information entailing to the tokens
 *              that make up the statement.
 */

import java.util.ArrayList;
import java.util.List;

public class Statement {

    // String version of the Statement being parsed
    private String statement = "";
    // List version of the Statement with all Token variables
    private List<Token> tokenStatement = new ArrayList<>();

    // Col variable used for error control by keeping same token from being added twice.
    private int col = 0;

    /**
     *  checks with duplicateCheck() to make sure
     *  token being passed has not already been added into
     *  the statement.
     *
     * @param token must not be NULL
     */
    public void createStatement(Token token){
        if(duplicateCheck(token)){
            tokenStatement.add(token);
        }
    }

    /**Converts the token list into a String that can be printed **/
    private void createTokenString(){
        int index = 0;
        while(index < tokenStatement.size()){
            Token tok = tokenStatement.get(index);
            statement = statement + " " + tok.getLexeme();
            index++;
        }
    }

    /**
     * Checks to make sure same token not added twice to statement
     * @param token must not be NULL
     * @return  true if token has not been added
     *          false if the token has been added
     */
    private boolean duplicateCheck(Token token){
        if(col < token.getColumnNumber()){
            col = token.getColumnNumber();
            return true;
        }
        return false;
    }
    /** @return string version of the statement **/
    public String getStringStatement(){
        createTokenString();
        return statement;
    }
    /** @return Token List version of the statement with all token variables **/
    public List<Token> getTokenStatement(){
        return tokenStatement;
    }
}

