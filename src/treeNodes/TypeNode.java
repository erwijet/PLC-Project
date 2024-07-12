package treeNodes;

import provided.JottType;
import provided.SemanticException;
import provided.SemanticValidationContext;
import provided.ParseContext;
import provided.ParseException;
import provided.Token;
import provided.TokenType;

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

        switch (node.token.getTokenString()) {
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
        return token.getTokenString();
    }

    @Override
    public void validateTree(SemanticValidationContext ctx) {
        switch (variant) {
            case DOUBLE:
            case INTEGER:
            case STRING:
            case BOOLEAN:
                break;
            default: // shouldnt happen but just in case
                throw new SemanticException(SemanticException.Cause.TYPE_CONFLICT, token, "Double, Integer, String, Boolean", variant);
        }
    }
    public JottType resolveType(SemanticValidationContext ctx) {
        return variant;
    }
}
