package jott; /**
 * This class is responsible for paring Jott Tokens
 * into a Jott parse tree.
 *
 * @author Tyler Holewinski
 */

import jott.parsing.ParseContext;
import jott.parsing.nodes.ProgramNode;
import jott.tokenization.Token;
import jott.tokenization.TokenType;

import java.util.ArrayList;

public class JottParser {

    /**
     * Parses an ArrayList of Jotton tokens into a Jott Parse Tree.
     * @param tokens the ArrayList of Jott tokens to parse
     * @return the root of the Jott Parse Tree represented by the tokens.
     *         or null upon an error in parsing.
     */
    public static JottTree parse(ArrayList<Token> tokens) {
        ProgramNode ret = null;

        try {
            ret = ProgramNode.parse(new ParseContext(tokens));
            return ret;
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            return null;
        }
    }
}
