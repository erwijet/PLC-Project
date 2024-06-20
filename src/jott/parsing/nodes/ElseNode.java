package jott.parsing.nodes;

import jott.parsing.ParseContext;
import jott.tokenization.TokenType;

public class ElseNode extends JottNode {
    private BodyStmtNode body;

    public static ElseNode parse(ParseContext ctx){
        ElseNode node = new ElseNode(); // not actually sure if making a node is needed?
        ctx.eat(TokenType.ID_KEYWORD, "Else");
        ctx.eat(TokenType.L_BRACE);
        node.body = BodyStmtNode.parse(ctx);
        ctx.eat(TokenType.R_BRACKET);
        return node;
    }

    @Override
    public String convertToJott() {
        return "Else{\n" + body.convertToJott() + "\n}";
    }
}
