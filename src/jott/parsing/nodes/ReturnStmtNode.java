package jott.parsing.nodes;

import jott.JottType;
import jott.SemanticException;
import jott.SymbolTable;
import jott.ValidationContext;
import jott.parsing.ParseContext;
import jott.tokenization.Token;
import jott.tokenization.TokenType;

import java.util.Optional;

public class ReturnStmtNode extends JottNode {
    Token kwd;
    ExprNode expr;
    boolean isEmpty = true;

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
    public void validateTree(ValidationContext ctx) {
        SymbolTable.Function enclosingFn = ctx.getEnclosingFunction().orElseThrow(() ->
            new SemanticException(SemanticException.Cause.UNEXPECTED_RETURN, expr.getFirstToken()));

        if (enclosingFn.isVoid() && !isEmpty)
            throw new SemanticException(SemanticException.Cause.TYPE_CONFLICT,
                    expr.getFirstToken(),
                    "Void",
                    expr.resolveType(ctx));

        if (isEmpty && !enclosingFn.isVoid()) {
            throw new SemanticException(SemanticException.Cause.TYPE_CONFLICT,
                    kwd,
                    "Void",
                    enclosingFn.returnType);
            }

        if (isEmpty && enclosingFn.isVoid()) return; // null expr

        JottType exprType = expr.resolveType(ctx);

        if (exprType != enclosingFn.returnType)
            throw new SemanticException(SemanticException.Cause.TYPE_CONFLICT,
                    expr.getFirstToken(),
                    enclosingFn.returnType,
                    exprType);
    }
}
