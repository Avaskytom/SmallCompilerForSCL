/**
 * The purpose of this class is to evaluate statements already reconized and excepted by the Parser.
 * These statements will then be evaluated based off the BNF rules selected and made into a executable
 * form.
 */

import java.util.List;

public class Evaluate {
    private List<Token> currentStatement;
    private String statementString;
    private String FileString = " ";
    private int index;

    /**
     * Constructor method for the Evaluate class. this will look at the first token
     * in each statement and determine what is the correct interpretation course based
     * off said token.
     * @param s must not be null
     */
    public Evaluate(Statement s){
        currentStatement = s.getTokenStatement();
        statementString = s.getStringStatement();
        index = 0;
        switch(currentStatement.get(index).getLexeme()){
            case "function":
                functionMain();
                break;
            case "define":
                define();
                break;
            case "display":
                display();
                break;
            case "set":
                setVariable();
                break;
            case "input":
                input();
                break;
            case "return":
                returnFun();
                break;
            case "endfun":
                endFun();
                break;
            default:
                break;

        }
    }

    /**
     * @return the token lexeme in the statement at the current index
     */
    private String getLexeme(){
        return currentStatement.get(index).getLexeme();
    }

    /**
     * increment the statement index
     */
    private void incrementIndex(){
        index++;
    }

    /**
     * creates the function header
     */
    private void functionMain(){
        incrementIndex();
        FileString = "public static int " + getLexeme() + "(String[] args){";
    }

    /**
     * creates and declares the variables by interpreting
     * VARIABLE of type TYPE into TYPE VARIABLE which is
     * reconized by java
     */
    private void define(){
        // define VARIABLE of type TYPE -> TYPE VARIABLE
        incrementIndex();
        String variable = getLexeme();
        incrementIndex();
        incrementIndex();
        incrementIndex();
        String type = getLexeme();

        FileString = type + " " + variable + ";";
    }

    /**
     * converts display SCL commands into Java system.out.print commands.
     */
    private void display(){
        incrementIndex();
        FileString = "System.out.println(";
        while(index<currentStatement.size()){
            if(getLexeme().equals(",")){
                FileString = FileString + "+";
            }else {
                FileString = FileString + " " + getLexeme();
            }
            incrementIndex();
        }
        FileString = FileString + ");";
    }

    /**
     * Declares the variables created in the source file
     */
    private void setVariable(){
        // SET VARIABLE = ARGUMENT
        incrementIndex(); // x
        FileString = getLexeme() + "= ";
        incrementIndex(); // =
        incrementIndex(); // 45.95
        FileString = FileString + getLexeme() + ";";
    }

    /**
     * Converts input into a Scanner input variable.
     */
    private void input(){
        FileString = "Scanner input = new Scanner(System.in); \n";
        incrementIndex();
        FileString = FileString + "System.out.println(" + getLexeme() + "); \n";
        incrementIndex();
        incrementIndex();
        FileString = FileString + getLexeme() + " = input.nextDouble();";
    }

    /** Simple return function. just converts this into java*/
    private void returnFun(){
        while(index < currentStatement.size()){
            if(FileString.isEmpty()){
                FileString = getLexeme() + " ";
            }else{
                FileString = FileString + getLexeme() + " ";
            }
            incrementIndex();
        }
        FileString = FileString + ";";
    }

    private void endFun(){
        FileString = "\t }";
    }

    public String getFileString(){
        return FileString;
    }
}
