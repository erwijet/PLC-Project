package jott.parsing.nodes;

import jott.SemanticException;
import jott.ValidationContext;
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

    @Override
    public void validateTree(ValidationContext ctx) { // this one might also just not be needed as this is handled in parsing, but if it needs code i do have it
        // pass
    }
}
