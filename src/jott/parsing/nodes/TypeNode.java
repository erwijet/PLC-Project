package jott.parsing.nodes;

import jott.parsing.ParseContext;
import jott.parsing.ParseException;
import jott.tokenization.Token;
import jott.tokenization.TokenType;

import java.util.List;

public class TypeNode extends JottNode {
    enum Variant {
        DOUBLE,
        INTEGER,
        STRING,
        BOOLEAN
    }

    private Variant variant;
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
                node.variant = Variant.DOUBLE;
            }
            case "Integer" -> {
                node.variant = Variant.INTEGER;
            }
            case "String" -> {
                node.variant = Variant.STRING;
            }
            case "Boolean" -> {
                node.variant = Variant.BOOLEAN;
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
