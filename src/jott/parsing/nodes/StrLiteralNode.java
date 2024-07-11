package jott.parsing.nodes;

import jott.parsing.ParseContext;
import jott.tokenization.Token;
import jott.tokenization.TokenType;

public class StrLiteralNode extends JottNode {
    Token token;

    public static StrLiteralNode parse(ParseContext ctx) {
        StrLiteralNode node = new StrLiteralNode();
        node.token = ctx.eat(TokenType.STRING);
        return node;
    }

    @Override
    public String convertToJott() {
        return token.getTokenString();
    }
}
