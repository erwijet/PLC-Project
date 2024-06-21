package jott.parsing.nodes;

import jott.parsing.ParseContext;
import jott.tokenization.TokenType;

public class ParamsTNode extends JottNode {
    ExprNode expr;

    public static ParamsTNode parse(ParseContext ctx) {
        ParamsTNode node = new ParamsTNode();
        ctx.eat(TokenType.COMMA);
        node.expr = ExprNode.parse(ctx);
        return node;
    }

    @Override
    public String convertToJott() {
        return null;
    }
}
