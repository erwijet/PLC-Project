package treeNodes;

import provided.SemanticException;
import provided.SemanticValidationContext;
import provided.ParseContext;

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

    public boolean hasReturn() {
        return body.hasReturn();
    }

    @Override
    public String convertToJott() {
        var ret = new StringBuilder();
        varDecls.forEach(it -> ret.append(it.convertToJott()).append("\n"));
        ret.append(body.convertToJott());
        return ret.toString();
    }

    @Override
    public void validateTree(SemanticValidationContext ctx) {
        varDecls.forEach(node -> node.validateTree(ctx));
        body.validateTree(ctx);

        ctx.getEnclosingFunction().ifPresent(fn -> {
            if (!fn.isVoid() && !hasReturn())
                throw new SemanticException(SemanticException.Cause.MISSING_RETURN, fn.token);
        });
    }
}
