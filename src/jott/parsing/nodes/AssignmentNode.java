package jott.parsing.nodes;


import jott.parsing.ParseContext;
import jott.tokenization.Token;
import jott.tokenization.TokenType;

public class AssignmentNode extends JottNode {
    private Token id;
    private ExprNode exp;

    static AssignmentNode parse(ParseContext ctx) {
        AssignmentNode node = new AssignmentNode();
        node.id = ctx.eat(TokenType.ID_KEYWORD);
        ctx.eat(TokenType.ASSIGN);
        node.exp = ExprNode.parse(ctx);
        ctx.eat(TokenType.SEMICOLON);
        return node;
    }

    @Override
    public String convertToJott() {
        return id.getToken() + " = " + exp.convertToJott() + ";";
    }
}
