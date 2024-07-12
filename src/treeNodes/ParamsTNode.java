package treeNodes;

import provided.JottType;
import provided.SemanticValidationContext;
import provided.ParseContext;
import provided.TokenType;

public class ParamsTNode extends JottNode {
    ExprNode expr;

    public static ParamsTNode parse(ParseContext ctx) {
        ParamsTNode node = new ParamsTNode();
        ctx.eat(TokenType.COMMA);
        node.expr = ExprNode.parse(ctx);
        return node;
    }

    @Override
    public String convertToJott() {
        return "," + expr.convertToJott();
    }

    public JottType resolveType(SemanticValidationContext ctx) {
        return expr.resolveType(ctx);
    }

    @Override
    public void validateTree(SemanticValidationContext ctx) {
        expr.validateTree(ctx);
    }
}
