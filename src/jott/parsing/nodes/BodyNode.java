package jott.parsing.nodes;

import jott.SemanticException;
import jott.ValidationContext;
import jott.parsing.ParseContext;

import java.util.LinkedList;
import java.util.List;

public class BodyNode extends JottNode {
    List<BodyStmtNode> bodyStmts;
    ReturnStmtNode returnStmt;

    public static BodyNode parse(ParseContext ctx) {
        // < body > -> < body_stmt >* < return_stmt >
        BodyNode node = new BodyNode();
        node.bodyStmts = new LinkedList<>();

        while (BodyStmtNode.canParse(ctx)) {
            node.bodyStmts.add(BodyStmtNode.parse(ctx));
        }

        node.returnStmt = ReturnStmtNode.parse(ctx);

        return node;
    }

    @Override
    public String convertToJott() {
        String str = "";
        for (BodyStmtNode stmt : bodyStmts) str += stmt.convertToJott() + " ";
        return str + returnStmt.convertToJott();
    }

    @Override
    public void validateTree(ValidationContext ctx) {
        bodyStmts.forEach(stmt -> stmt.validateTree(ctx));

        ctx.getEnclosingFunction().ifPresent(fn -> {
            if (!fn.isVoid() && returnStmt == null)
                throw new SemanticException(SemanticException.Cause.MISSING_RETURN, fn.token);
        });

        if (returnStmt != null)
            returnStmt.validateTree(ctx);
    }
}
