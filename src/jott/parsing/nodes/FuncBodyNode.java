package jott.parsing.nodes;

import java.util.List;

public class FuncBodyNode extends JottNode {
    List<VarDeclNode> varDecls;
    BodyNode body;

    @Override
    public String convertToJott() {
        return null;
    }
}
