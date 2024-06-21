package jott.parsing.nodes;

import jott.parsing.ParseContext;
import jott.tokenization.TokenType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ParamsNode extends JottNode {
    ExprNode expr;
    List<ParamsTNode> tail;

    public static ParamsNode parse(ParseContext ctx) {
        ParamsNode node = new ParamsNode();
        node.expr = ExprNode.parse(ctx);
        node.tail = new LinkedList<>();

        while (ctx.peekNextType() == TokenType.COMMA) {
            node.tail.add(ParamsTNode.parse(ctx));
        }

        return node;
    }

    @Override
    public String convertToJott() {
        return null;
    }
}
