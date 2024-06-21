package jott.parsing.nodes;

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
        return null;
    }
}
