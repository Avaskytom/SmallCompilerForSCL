import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;


public class CompilerTester {

    public static void main(String[] args) throws FileNotFoundException{

        String filename = "C:\\Users\\Grady\\IdeaProjects\\Scanner\\src\\scl.txt";
        LexScanner lex = new LexScanner(filename);
        Parser parse = new Parser(lex.getTokens());
        Interpreter interpreter = new Interpreter(parse.getTokenStatements());


        // Try catch to write the file using the interpreted statements.
        try{
            File file = new File("C:\\Users\\Grady\\IdeaProjects\\Scanner\\src\\SCL.java");

            if(!file.exists()){
                file.createNewFile();
            }

            PrintWriter writer = new PrintWriter(file);
            writer.println("import java.util.Scanner;");
            writer.println("public class SCL { ");
            int index = 0;
            while(index < interpreter.getFileLines().size()){

                String state = interpreter.getFileLines().get(index);
                writer.println(state);
                index++;
            }
            writer.println("}");
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        String finalFile = "SCL.java";
        JavaCompiler comp = ToolProvider.getSystemJavaCompiler();

        SCL.main(null);


    }

}
