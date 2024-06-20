package jott.parsing.nodes;

import jott.parsing.ParseContext;
import jott.tokenization.Token;
import jott.tokenization.TokenType;

public class MathOpNode extends JottNode {
    Token token;

    public static MathOpNode parse(ParseContext ctx) {
        MathOpNode node = new MathOpNode();
        node.token = ctx.eat(TokenType.MATH_OP);
        return node;
    }

    @Override
    public String convertToJott() {
        return token.getToken();
    }
}
