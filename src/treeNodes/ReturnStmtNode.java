package treeNodes;

import provided.JottType;
import provided.SemanticException;
import provided.SymbolTable;
import provided.SemanticValidationContext;
import provided.ParseContext;
import provided.Token;
import provided.TokenType;

public class ReturnStmtNode extends JottNode {
    Token kwd;
    ExprNode expr;
    public boolean isEmpty = true;

    public static ReturnStmtNode parse(ParseContext ctx) {
        ReturnStmtNode node = new ReturnStmtNode();

        if (ctx.peekIs(TokenType.ID_KEYWORD, "Return")) {
            node.kwd = ctx.eat(TokenType.ID_KEYWORD, "Return");
            node.expr = ExprNode.parse(ctx);
            ctx.eat(TokenType.SEMICOLON);
            node.isEmpty = false;
        }

        return node;
    }

    @Override
    public String convertToJott() {
        return isEmpty ? "" : "Return " + expr.convertToJott() + ";";
    }

    @Override
    public String convertToC() {
        if(isEmpty)
            return "return 1;";
        return "return " + expr.convertToC() + ";";
    }

    @Override
    public String convertToJava(String className) {
        return isEmpty ? "" : "return " + expr.convertToJava(className) + ";";
    }

    @Override
    public String convertToPython() {
        return isEmpty ? "" : "return " + expr.convertToPython() + ";";
    }

    @Override
    public void validateTree(SemanticValidationContext ctx) {
        SymbolTable.Function enclosingFn = ctx.getEnclosingFunction().orElseThrow(() ->
            new SemanticException(SemanticException.Cause.UNEXPECTED_RETURN, expr.getFirstToken()));

        if (enclosingFn.isVoid() && !isEmpty)
            throw new SemanticException(SemanticException.Cause.TYPE_CONFLICT,
                    expr.getFirstToken(),
                    "Void",
                    expr.resolveType(ctx));

        if (isEmpty) return; // null expr

        JottType exprType = expr.resolveType(ctx);
        if (exprType != enclosingFn.returnType)
            throw new SemanticException(SemanticException.Cause.TYPE_CONFLICT,
                    expr.getFirstToken(),
                    enclosingFn.returnType,
                    exprType);
    }
}
