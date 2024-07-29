package treeNodes;

import provided.SemanticException;
import provided.SemanticValidationContext;
import provided.ParseContext;
import provided.ParseException;
import provided.Token;
import provided.TokenType;

import java.util.LinkedList;
import java.util.List;

public class ProgramNode extends JottNode {
    List<FuncDefNode> functions;

    public static ProgramNode parse(ParseContext ctx){
        // < program > -> < function_def >* <EOF >
        ProgramNode node = new ProgramNode();
        node.functions = new LinkedList<>();

        while(ctx.peekNextType() == TokenType.ID_KEYWORD)
            node.functions.add(FuncDefNode.parse(ctx));

        // expect EOF

        if (!ctx.isEOF()) {
            throw new ParseException(ParseException.Cause.UNUSED_TOKENS, ctx, null);
        }

        return node;
    }
    @Override
    public String convertToJott() {
        var ret = new StringBuilder();

        for (FuncDefNode function : functions)
            ret.append(function.convertToJott()).append("\n");

        return ret.toString();
    }

    @Override
    public String convertToC() {
        var ret = new StringBuilder();
        if(!functions.isEmpty()) // not sure if we put these on a blank file
            ret.append("""
                #include <stdio.h>
                #include <stdbool.h>
                #include <string.h>
                #include <stdlib.h>""");
        for (FuncDefNode function : functions)
            ret.append(function.convertToC()).append("\n");
        return ret.toString();
    }

    @Override
    public String convertToJava(String className) {
        var ret = new StringBuilder();
        if(!functions.isEmpty()) // not sure if we put this on a blank file
            ret.append("public class ").append(className).append(" {");
        for (FuncDefNode function : functions)
            ret.append(function.convertToC()).append("\n");
        return ret + "}";
    }

    @Override
    public String convertToPython() {
        var ret = new StringBuilder();
        for (FuncDefNode function : functions)
            ret.append(function.convertToJott()).append("\n");
        if(!functions.isEmpty()) // not sure if add on blank file
            ret.append("main()");
        return ret.toString();
    }

    @Override
    public void validateTree(SemanticValidationContext ctx) {
        boolean main = false;
        Token t = null;
        for(FuncDefNode node : functions) {
            node.validateTree(ctx);
            t = node.funcId;
            if(node.funcId.getTokenString().equalsIgnoreCase("main"))
                main = true;
        }
        if(!main) // no main
            throw new SemanticException(SemanticException.Cause.MISSING_MAIN, t);
    }
}
