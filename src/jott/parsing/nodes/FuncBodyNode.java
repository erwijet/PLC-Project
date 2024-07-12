package jott.parsing.nodes;

import jott.JottType;
import jott.SemanticException;
import jott.ValidationContext;
import jott.parsing.ParseContext;

import java.util.LinkedList;
import java.util.List;

public class FuncBodyNode extends JottNode {
    List<VarDeclNode> varDecls;
    BodyNode body;

    public static FuncBodyNode parse(ParseContext ctx) {
        FuncBodyNode node = new FuncBodyNode();
        node.varDecls = new LinkedList<>();

        while (VarDeclNode.canParse(ctx)) {
            node.varDecls.add(VarDeclNode.parse(ctx));
        }

        node.body = BodyNode.parse(ctx);
        return node;
    }

    @Override
    public String convertToJott() {
        var ret = new StringBuilder();
        varDecls.forEach(it -> ret.append(it.convertToJott()).append("\n"));
        ret.append(body.convertToJott());
        return ret.toString();
    }

    @Override
    public void validateTree(ValidationContext ctx) {
        varDecls.forEach(node -> node.validateTree(ctx));
        body.validateTree(ctx);

        ctx.getEnclosingFunction().ifPresent(fn -> {
            if (!fn.isVoid() && body.returnStmt.isEmpty)
                throw new SemanticException(SemanticException.Cause.MISSING_RETURN, fn.token);
        });
    }
}
