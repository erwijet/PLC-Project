package treeNodes;

import provided.SemanticValidationContext;
import provided.ParseContext;
import provided.Token;
import provided.TokenType;

public class MathOpNode extends JottNode {
    Token token;

    public static MathOpNode parse(ParseContext ctx) {
        MathOpNode node = new MathOpNode();
        node.token = ctx.eat(TokenType.MATH_OP);
        return node;
    }

    @Override
    public String convertToJott() {
        return token.getTokenString();
    }

    @Override
    public void validateTree(SemanticValidationContext ctx) {
        // pass
    }
}
