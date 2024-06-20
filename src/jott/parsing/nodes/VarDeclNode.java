package jott.parsing.nodes;

import jott.parsing.ParseContext;
import jott.tokenization.Token;
import jott.tokenization.TokenType;

public class VarDeclNode extends JottNode {
    TypeNode type;
    Token identifier;

    public static VarDeclNode parse(ParseContext ctx) {
        // < var_dec > -> < type > < id >;
        VarDeclNode node = new VarDeclNode();
        node.type = TypeNode.parse(ctx);
        node.identifier = ctx.eat(TokenType.ID_KEYWORD);

        return node;
    }

    @Override
    public String convertToJott() {
        return type.convertToJott() + identifier.getToken() + ";";
    }
}
