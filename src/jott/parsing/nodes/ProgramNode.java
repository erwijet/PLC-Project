package jott.parsing.nodes;

import jott.ValidationContext;
import jott.parsing.ParseContext;
import jott.parsing.ParseException;
import jott.tokenization.TokenType;

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
    public void validateTree(ValidationContext ctx) {
        functions.forEach(node -> node.validateTree(ctx));
    }
}
