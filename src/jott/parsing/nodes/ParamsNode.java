package jott.parsing.nodes;

import jott.JottTree;
import jott.JottType;
import jott.ValidationContext;
import jott.parsing.ParseContext;
import jott.tokenization.TokenType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ParamsNode extends JottNode {
    ExprNode expr;
    List<ParamsTNode> tail;

    public static ParamsNode parse(ParseContext ctx) {
        ParamsNode node = new ParamsNode();
        node.expr = ExprNode.parse(ctx);
        node.tail = new LinkedList<>();

        while (ctx.peekNextType() == TokenType.COMMA) {
            node.tail.add(ParamsTNode.parse(ctx));
        }

        return node;
    }

    @Override
    public String convertToJott() {
        var ret = new StringBuilder();
        ret.append(expr.convertToJott());
        tail.stream().map(JottTree::convertToJott).forEach(ret::append);
        return ret.toString();
    }

    public List<JottType> resolveParameterTypes(ValidationContext ctx) {
        List<JottType> types = new LinkedList<>(List.of(expr.resolveType(ctx)));
        tail.forEach(param -> types.add(param.resolveType(ctx)));
        return types;
    }

    @Override
    public void validateTree(ValidationContext ctx) {
        expr.validateTree(ctx);
        tail.forEach(node -> node.validateTree(ctx));
    }
}
