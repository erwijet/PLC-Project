package jott.parsing.nodes;

import jott.parsing.ParseContext;
import jott.tokenization.Token;
import jott.tokenization.TokenType;

public class TypeNode extends JottNode {
    enum Variant {
        DOUBLE,
        INTEGER,
        STRING,
        BOOLEAN
    }

    private Variant variant;
    private Token token;

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
            default -> throw new RuntimeException("Expected any of 'Double', 'Integer', 'String', 'Boolean', found " + node.token.getToken() + " for Type");
        };

        return node;
    }

    @Override
    public String convertToJott() {
        return null;
    }
}
