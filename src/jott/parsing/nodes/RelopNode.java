package jott.parsing.nodes;

import jott.parsing.ParseContext;
import jott.tokenization.Token;
import jott.tokenization.TokenType;

public class RelopNode extends JottNode {
    Token token;

    public static RelopNode parse(ParseContext ctx) {
        RelopNode node = new RelopNode();
        node.token = ctx.eat(TokenType.REL_OP);
        return node;
    }

    @Override
    public String convertToJott() {
        return null;
    }
}
