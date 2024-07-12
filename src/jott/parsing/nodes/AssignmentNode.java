package jott.parsing.nodes;


import jott.JottType;
import jott.SemanticException;
import jott.SymbolTable;
import jott.ValidationContext;
import jott.parsing.ParseContext;
import jott.tokenization.Token;
import jott.tokenization.TokenType;

public class AssignmentNode extends JottNode {
    private Token id;
    private ExprNode exp;

    public static boolean canParse(ParseContext ctx) {
        return ctx.peekNextType() == TokenType.ID_KEYWORD;
    }

    public static AssignmentNode parse(ParseContext ctx) {
        // < asmt > -> <id >= < expr >;
        AssignmentNode node = new AssignmentNode();
        node.id = ctx.eat(TokenType.ID_KEYWORD);
        ctx.eat(TokenType.ASSIGN);
        node.exp = ExprNode.parse(ctx);
        ctx.eat(TokenType.SEMICOLON);
        return node;
    }

    @Override
    public String convertToJott() {
        return id.getTokenString() + " = " + exp.convertToJott() + ";";
    }

    @Override
    public void validateTree(ValidationContext ctx) {
        exp.validateTree(ctx);

        JottType expectedType = ctx.table.resolve(id.getTokenString(), SymbolTable.Binding.class)
                .orElseThrow(() -> new SemanticException(SemanticException.Cause.UNKNOWN_BINDING, id))
                .type;

        JottType foundType = exp.resolveType(ctx);

        if (expectedType != foundType) {
            throw new SemanticException(SemanticException.Cause.TYPE_CONFLICT, id, expectedType, foundType);
        }
    }
}
