import java.util.ArrayList;
import java.util.List;

public class Interpreter {

    private List<Statement> statements;
    private List<String> fileLines = new ArrayList<>();
    private int stateIndex;

    public Interpreter(List<Statement> statements){
        this.statements = statements;
        stateIndex = 0;

        while(stateIndex < statements.size()){
            Evaluate eval = new Evaluate(this.statements.get(stateIndex));
            fileLines.add(eval.getFileString());
            stateIndex++;
        }

    }

    public List<String> getFileLines(){
        return fileLines;
    }

}
