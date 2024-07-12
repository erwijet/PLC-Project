package treeNodes;

import provided.SemanticValidationContext;
import provided.ParseContext;
import provided.Token;
import provided.TokenType;

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
    public void validateTree(SemanticValidationContext ctx) { // this one might also just not be needed as this is handled in parsing, but if it needs code i do have it
        // pass
    }
}
