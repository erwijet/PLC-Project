package jott.parsing.nodes;

import jott.parsing.ParseContext;
import jott.tokenization.TokenType;

public class ReturnStmtNode extends JottNode {
    ExprNode expr;

    public static ReturnStmtNode parse(ParseContext ctx) {
        ReturnStmtNode node = new ReturnStmtNode();
        ctx.eat(TokenType.ID_KEYWORD, "Return");
        node.expr = ExprNode.parse(ctx);

        return node;
    }

    @Override
    public String convertToJott() {
        return expr.convertToJott();
    }
}
