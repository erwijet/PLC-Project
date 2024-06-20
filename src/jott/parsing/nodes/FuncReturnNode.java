package jott.parsing.nodes;

import jott.parsing.ParseContext;
import jott.tokenization.TokenType;

import java.util.Objects;

public class FuncReturnNode extends JottNode {
    public TypeNode type;
    public boolean isVoid() {
        return type == null;
    }

    public static FuncReturnNode parse(ParseContext ctx) {
        FuncReturnNode node = new FuncReturnNode();

        if (Objects.equals(ctx.peekNextStr(), "Void")) {
            ctx.eat(TokenType.ID_KEYWORD, "Void");
        }

        node.type = TypeNode.parse(ctx);
        return node;
    }

    @Override
    public String convertToJott() {
        return null;
    }
}
