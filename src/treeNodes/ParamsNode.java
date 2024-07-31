package treeNodes;

import provided.JottTree;
import provided.JottType;
import provided.SemanticValidationContext;
import provided.ParseContext;
import provided.TokenType;

import java.util.LinkedList;
import java.util.List;

public class ParamsNode extends JottNode {
    ExprNode expr;
    List<ParamsTNode> tail = new LinkedList<>();

    public static ParamsNode parse(ParseContext ctx) {
        ParamsNode node = new ParamsNode();
        if (!ExprNode.canParse(ctx)) return node;

        node.expr = ExprNode.parse(ctx);

        while (ctx.peekNextType() == TokenType.COMMA) {
            node.tail.add(ParamsTNode.parse(ctx));
        }

        return node;
    }

    @Override
    public String convertToJott() {
        var ret = new StringBuilder();
        if (expr == null) return "";

        ret.append(expr.convertToJott());
        tail.stream().map(JottTree::convertToJott).forEach(ret::append);
        return ret.toString();
    }

    @Override
    public String convertToC() {
        if (expr == null) return "";
        var ret = new StringBuilder();
        ret.append(expr.convertToC());
        tail.stream().map(JottTree::convertToC).forEach(ret::append);
        return ret.toString();
    }

    @Override
    public String convertToJava(String className) {
        if (expr == null) return "";
        var ret = new StringBuilder();
        ret.append(expr.convertToJava(className));
        tail.stream().map(t -> t.convertToJava(className)).forEach(ret::append);
        return ret.toString();
    }

    @Override
    public String convertToPython() {
        if (expr == null) return "";
        var ret = new StringBuilder();
        ret.append(expr.convertToPython());
        tail.stream().map(JottTree::convertToPython).forEach(ret::append);
        return ret.toString();
    }

    public List<JottType> resolveParameterTypes(SemanticValidationContext ctx) {
        if (expr == null) return List.of();
        
        List<JottType> types = new LinkedList<>(List.of(expr.resolveType(ctx)));
        tail.forEach(param -> types.add(param.resolveType(ctx)));
        return types;
    }

    @Override
    public void validateTree(SemanticValidationContext ctx) {
        expr.validateTree(ctx);
        tail.forEach(node -> node.validateTree(ctx));
    }
}
