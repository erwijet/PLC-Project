package jott;

import jott.parsing.ParseException;
import jott.tokenization.JottTokenizationException;
import jott.tokenization.JottTokenizer;
import jott.tokenization.Token;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Jott {
    public static void main(String[] args) {
        if(args.length != 3) { // incorrect number of args
            System.out.println("Usage: java Jott <input_file> <output_file> <output_language>");
            return;
        }
        String input = args[0];
        String output = args[1];
        String language;
        if (!((language = args[2].toLowerCase()).equals("jott") || language.equals("java") || language.equals("python") || language.equals("c"))) { // unsupported language
            System.out.println("Error: <output_language> must be either 'jott', 'java', 'python', or 'c'. " + language + " is not supported");
            return;
        }
        try{
            File in = new File(input);
            if(!in.exists() || !in.isFile() || !in.canRead()) { // makes sure input file is legit
                System.out.println("Error: input file " + input + " does not exist, is not a file, or is not readable.");
                return;
            }
            ArrayList<Token> tokens = JottTokenizer.tokenize(input);
            JottTree tree = JottParser.parse(tokens);
            assert tree != null;
            tree.validateTree()
                String code = "";
                if (language.equalsIgnoreCase("jott"))
                    code += tree.convertToJott();
                else if (language.equalsIgnoreCase("java")) // todo: get proper class name
                    code += tree.convertToJava("");
                else if (language.equalsIgnoreCase("python"))
                    code += tree.convertToPython();
                else
                    code += tree.convertToC();
                BufferedWriter writer = new BufferedWriter(new FileWriter(output));
                writer.write(code);
                writer.close();
        }
        catch (IOException e){ // not valid output file
            System.out.println("Error: output file " + output + "is not a valid file or is not readable/writeable.");
        }
        catch(ParseException | JottTokenizationException | SemanticException e){ // tokenizer, parser, semantic exception
        }
    }
}
