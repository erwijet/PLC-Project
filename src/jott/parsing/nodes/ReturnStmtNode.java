package jott.parsing.nodes;

import jott.parsing.ParseContext;
import jott.tokenization.TokenType;

public class ReturnStmtNode extends JottNode {
    ExprNode expr;
    boolean isEmpty = true;

    public static ReturnStmtNode parse(ParseContext ctx) {
        ReturnStmtNode node = new ReturnStmtNode();

        if (ctx.peekIs(TokenType.ID_KEYWORD, "Return")) {
            ctx.eat(TokenType.ID_KEYWORD, "Return");
            node.expr = ExprNode.parse(ctx);
            node.isEmpty = false;
        }

        return node;
    }

    @Override
    public String convertToJott() {
        return isEmpty ? "" : expr.convertToJott();
    }
}
