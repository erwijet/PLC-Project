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
