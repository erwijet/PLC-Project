package jott.parsing.nodes;

import jott.JottType;
import jott.parsing.ParseContext;
import jott.parsing.ParseException;
import jott.tokenization.Token;
import jott.tokenization.TokenType;

import java.util.List;

public class TypeNode extends JottNode {

    private JottType variant;
    private Token token;

    static boolean canParse(ParseContext ctx) {
        return ctx.peekNextType() == TokenType.ID_KEYWORD
                && List.of("Double", "Integer", "String", "Boolean").contains(ctx.peekNextStr());
    }

    static TypeNode parse(ParseContext ctx) {
        TypeNode node = new TypeNode();
        node.token = ctx.eat(TokenType.ID_KEYWORD);

        switch (node.token.getToken()) {
            case "Double" -> {
                node.variant = JottType.DOUBLE;
            }
            case "Integer" -> {
                node.variant = JottType.INTEGER;
            }
            case "String" -> {
                node.variant = JottType.STRING;
            }
            case "Boolean" -> {
                node.variant = JottType.BOOLEAN;
            }
            default -> throw new ParseException(ParseException.Cause.SYNTAX, ctx, "Double, Integer, String, or Boolean");
        };

        return node;
    }

    @Override
    public String convertToJott() {
        return token.getToken();
    }
}
