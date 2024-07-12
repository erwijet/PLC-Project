package jott.parsing.nodes;

import jott.ValidationContext;
import jott.parsing.ParseContext;
import jott.tokenization.TokenType;

import java.util.Objects;

public class ElseNode extends JottNode {
    BodyNode body;
    Boolean empty;

    public static ElseNode parse(ParseContext ctx){
        // < else > -> Else { < body >} | empty
        ElseNode node = new ElseNode(); // not actually sure if making a node is needed?
        if(Objects.equals(ctx.peekNextStr(), "Else")){
            ctx.eat(TokenType.ID_KEYWORD, "Else");
            ctx.eat(TokenType.L_BRACE);
            node.body = BodyNode.parse(ctx);
            ctx.eat(TokenType.R_BRACE);
            node.empty = Boolean.FALSE;
            return node;
        }
        node.empty = Boolean.TRUE;
        return null; // if no else exists
    }

    public boolean hasReturn() {
        return body.hasReturn();
    }

    @Override
    public String convertToJott() {
        if (!empty)
            return "Else{\n" + body.convertToJott() + "\n}";
        return "";
    }

    @Override
    public void validateTree(ValidationContext ctx) {
        if (empty) return;
        ctx.table.pushScope();
        body.validateTree(ctx);
        ctx.table.popScope();
    }
}
