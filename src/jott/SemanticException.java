package jott;

import jott.tokenization.Token;

public class SemanticException extends RuntimeException {
    public enum Cause {
        TYPE_CONFLICT,
        MISSING_RETURN_TYPE,
        UNKNOWN_FUNCTION,
        UNKNOWN_BINDING,
        MALFORMED_TREE
    }

    private static String buildMessage(Cause cause, Token token, Object expected, Object found) {
        return switch (cause) {
            case TYPE_CONFLICT -> String.format("Semantic Error\nInvalid type for '%s'. Expected %s, but found %s\n%s",
                    token.getTokenString(),
                    expected.toString(),
                    found.toString(),
                    token);
            case MISSING_RETURN_TYPE -> null;
            case UNKNOWN_FUNCTION -> null;
            case UNKNOWN_BINDING -> null;
            // this one really shouldn't ever happen. it's to handle if we expect a relop expr
            // to have 3 children (left, opt, right) and it has less or something
            case MALFORMED_TREE -> null;
        };
    }

    public SemanticException(Cause cause, Token token) {
        super(buildMessage(cause, token, null, null));
    }

    public SemanticException(Cause cause, Token token, Object expected, Object found) {
        super(buildMessage(cause, token, expected, found));
    }
}

