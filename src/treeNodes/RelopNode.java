package treeNodes;

import provided.SemanticValidationContext;
import provided.ParseContext;
import provided.Token;
import provided.TokenType;

public class RelopNode extends JottNode {
    Token token;

    public static RelopNode parse(ParseContext ctx) {
        RelopNode node = new RelopNode();
        node.token = ctx.eat(TokenType.REL_OP);
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
