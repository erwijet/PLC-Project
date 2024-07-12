package treeNodes;

import provided.SemanticValidationContext;
import provided.ParseContext;
import provided.TokenType;

import java.util.Objects;

public class FuncReturnNode extends JottNode {
    public TypeNode type;
    public boolean isVoid() {
        return type == null;
    }

    public static FuncReturnNode parse(ParseContext ctx) {
        // < function_return > -> < type > | Void
        FuncReturnNode node = new FuncReturnNode();

        if (Objects.equals(ctx.peekNextStr(), "Void")) {
            ctx.eat(TokenType.ID_KEYWORD, "Void");
        } else if(Objects.equals(ctx.peekNextStr(), "void"))
            ctx.eat(TokenType.ID_KEYWORD, "void");
        else {
            node.type = TypeNode.parse(ctx);
        }

        return node;
    }

    @Override
    public String convertToJott() {
        if(type != null)
            return type.convertToJott();
        return "Void";
    }

    @Override
    public void validateTree(SemanticValidationContext ctx) {
        // pass
    }
}
