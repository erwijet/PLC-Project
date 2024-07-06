package jott.parsing.nodes;

import jott.parsing.ParseContext;
import jott.tokenization.TokenType;

public class ElseIfNode extends JottNode {
    ExprNode condition;
    BodyNode body;

    public static ElseIfNode parse(ParseContext ctx){
        // < elseif > -> Elseif [ < expr >]{ < body >}
        ElseIfNode node = new ElseIfNode(); // not actually sure if making a node is needed?
        ctx.eat(TokenType.ID_KEYWORD, "Elseif");
        ctx.eat(TokenType.L_BRACKET);
        node.condition = ExprNode.parse(ctx);
        ctx.eat(TokenType.R_BRACKET);
        ctx.eat(TokenType.L_BRACE);
        node.body = BodyNode.parse(ctx);
        ctx.eat(TokenType.R_BRACKET);
        return node;
    }

    @Override
    public String convertToJott() {
        return "Elseif[" + condition.convertToJott() + "]{\n" + body.convertToJott() + "\n}";
    }
}
