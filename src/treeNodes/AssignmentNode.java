package treeNodes;


import provided.JottType;
import provided.SemanticException;
import provided.SymbolTable;
import provided.SemanticValidationContext;
import provided.ParseContext;
import provided.Token;
import provided.TokenType;

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
    public String convertToC() {
        if(id.getTokenString().equalsIgnoreCase("switch"))
            return "__" + id.getTokenString() + " = " + exp.convertToC() + ";";
        return id.getTokenString() + " = " + exp.convertToC() + ";";
    }

    @Override
    public String convertToJava(String className) {
        if(id.getTokenString().equalsIgnoreCase("switch"))
            return "__" + id.getTokenString() + " = " + exp.convertToJava(className) + ";";
        return id.getTokenString() + " = " + exp.convertToJava(className) + ";";
    }

    @Override
    public String convertToPython() {
        return id.getTokenString() + " = " + exp.convertToPython();
    }

    @Override
    public void validateTree(SemanticValidationContext ctx) {
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
