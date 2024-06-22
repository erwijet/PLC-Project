package jott.parsing.nodes;

import jott.parsing.ParseContext;
import jott.parsing.ParseException;
import jott.tokenization.Token;
import jott.tokenization.TokenType;

import java.util.Objects;

public class BoolNode extends JottNode {
    Token token;
    Variant variant;

    enum Variant {
        TRUE,
        FALSE
    }

    public static BoolNode parse(ParseContext ctx) {
        BoolNode node = new BoolNode();

        switch (ctx.peekNextStr()) {
            case "True" -> {
                node.token = ctx.eat(TokenType.ID_KEYWORD, "True");
                node.variant = Variant.TRUE;
            }
            case "False" -> {
                node.token = ctx.eat(TokenType.ID_KEYWORD, "False");
                node.variant = Variant.FALSE;
            }
            default -> {
                throw new ParseException(ParseException.Cause.SYNTAX, ctx, "True or False");
            }
        }

        return node;
    }

    @Override
    public String convertToJott() {
        return switch (this.variant) {
            case TRUE -> "True";
            case FALSE ->  "False";
        };
    }
}
