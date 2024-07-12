package jott;

import jott.parsing.ParseContext;
import jott.tokenization.JottTokenizerContext;
import jott.tokenization.Token;
import jott.tokenization.TokenType;

public class SemanticException extends RuntimeException {
    public enum Cause {
        TYPE_CONFLICT,
        UNKNOWN_FUNCTION,
        UNKNOWN_BINDING,
        INCORRECT_ARGUMENT_COUNT,
        MISSING_MAIN,
        MALFORMED_TREE,
        UNEXPECTED_RETURN,
        MISSING_RETURN,
        CONFLICTING_IDENTIFIER
    }

    private static String buildMessage(Cause cause, Token token, Object expected, Object found) {
        return switch (cause) {
            case TYPE_CONFLICT -> String.format("Semantic Error:\nInvalid type for '%s'. Expected %s, but found %s\n%s:%d",
                    token.getTokenString(),
                    expected.toString(),
                    found.toString(),
                    token.getFilename(),
                    token.getLineNum());
            case INCORRECT_ARGUMENT_COUNT -> String.format("Semantic Error:\nInvalid argument count for '%s'. Expected %s, but found %s\n%s:%d",
                    token.getTokenString(),
                    expected.toString(),
                    found.toString(),
                    token.getFilename(),
                    token.getLineNum());
            case MISSING_RETURN -> String.format("Semantic Error:\nMissing return for non-Void function %s\n%s:%d",
                    token.getTokenString(),
                    token.getFilename(),
                    token.getLineNum());
            case UNKNOWN_FUNCTION -> String.format("Semantic Error:\nCall to unknown function '%s'\n%s:%d",
                    token.getTokenString(),
                    token.getFilename(),
                    token.getLineNum());
            case UNKNOWN_BINDING -> String.format("Semantic Error:\nCall to unknown variable '%s'\n%s:%d",
                    token.getTokenString(),
                    token.getFilename(),
                    token.getLineNum());
            case MISSING_MAIN -> String.format("Semantic Error:\nMissing or incorrectly defined main function\n%s:%d",
                    token.getFilename(),
                    token.getLineNum());
            case CONFLICTING_IDENTIFIER -> String.format("Semantic Error:\nIdentifier '%s' already exists\n%s:%d",
                    expected,
                    token.getFilename(),
                    token.getLineNum());
            case UNEXPECTED_RETURN -> String.format("Semantic Error:\nUnexpected return statement. Not currently within a function\n%s:%d",
                    token.getFilename(),
                    token.getLineNum());
            // this one really shouldn't ever happen. it's to handle if we expect a relop expr
            // to have 3 children (left, opt, right) and it has less or something
            case MALFORMED_TREE -> String.format("Semantic Error:\nMalformed tree");
        };
    }

    public SemanticException(Cause cause, Token token) {
        super(buildMessage(cause, token, null, null));
    }

    public SemanticException(Cause cause, Token token, Object expected, Object found) {
        super(buildMessage(cause, token, expected, found));
    }
}


