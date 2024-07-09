package jott;

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
        String language = args[2];
        try{
            File in = new File(input);
            if(!in.exists() || !in.isFile() || !in.canRead()) { // makes sure input file is legit
                System.out.println("Error: input file " + input + " does not exist, is not a file, or is not readable.");
                return;
            }
            ArrayList<Token> tokens = JottTokenizer.tokenize(input); // todo: may need try catch here
            JottTree tree = JottParser.parse(tokens); // todo: prob need try catch here
            assert tree != null;
            if(tree.validateTree()) { // todo: needs to run entire tree which it isnt set up for currently
                String code = "";
                if (language.equalsIgnoreCase("jott"))
                    code += tree.convertToJott();
                else if (language.equalsIgnoreCase("java")) // todo: get proper class name
                    code += tree.convertToJava("");
                else if (language.equalsIgnoreCase("python"))
                    code += tree.convertToPython();
                else if (language.equalsIgnoreCase("c"))
                    code += tree.convertToC();
                else { // invalid language
                    System.out.println("Error: Language " + language + " is not supported. Please use 'Jott', 'Java', 'Python', or 'C'");
                    return;
                }
                BufferedWriter writer = new BufferedWriter(new FileWriter(output));
                writer.write(code);
                writer.close();
            }
            else
                System.out.println("Invalid tree");
        }
        catch (IOException e){
            System.out.println("Error: output file " + output + "is not a valid file or is not readable/writeable.");
            return;
        }
    }
}
