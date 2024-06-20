package jott.parsing.nodes;

import jott.parsing.ParseContext;
import jott.tokenization.TokenType;

public class IfStmtNode extends JottNode {
    private ExprNode condition;
    private BodyStmtNode body;

    public static IfStmtNode parse(ParseContext ctx){
        IfStmtNode node = new IfStmtNode(); // not actually sure if making a node is needed?
        ctx.eat(TokenType.ID_KEYWORD, "If");
        ctx.eat(TokenType.L_BRACKET);
        node.condition = ExprNode.parse(ctx);
        ctx.eat(TokenType.R_BRACKET);
        ctx.eat(TokenType.L_BRACE);
        node.body = BodyStmtNode.parse(ctx);
        ctx.eat(TokenType.R_BRACKET);
        return node;
    }

    @Override
    public String convertToJott() {
        return "If[" + condition.convertToJott() + "]{\n" + body.convertToJott() + "\n}";
    }
}
