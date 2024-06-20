package jott.parsing.nodes;

import jott.parsing.ParseContext;
import jott.tokenization.TokenType;

import java.util.LinkedList;
import java.util.List;

public class ProgramNode extends JottNode {
    List<FuncDefNode> functions;

    public static ProgramNode parse(ParseContext ctx){
        ProgramNode node = new ProgramNode();
        node.functions = new LinkedList<>();
        while(ctx.peekNextType() == TokenType.ID_KEYWORD)
            node.functions.add(FuncDefNode.parse(ctx)); // this is assuming that in funcdefnode, it calls all the following nodes
        return node;
    }
    @Override
    public String convertToJott() {
        String func = "";
        for (FuncDefNode function : functions) func += function.convertToJott() + "\n";
        return func;
    }
}
