package jott.tokenization;

public class JottTokenizationException extends Exception {
    public enum Cause {
        UNEXPECTED_CHARACTER
    }

    private static String getMessage(Cause cause, JottTokenizerContext ctx) {
        switch (cause) {
            case UNEXPECTED_CHARACTER -> {
                Character chr = ctx.peekNext();
                String current = chr == null || chr == '\0' ? "EOF" : chr.toString();

                return String.format("Unexpected character '%s' on line %s", current, ctx.lineNumber);
            }
        }

        return "Unknown cause";
    }

    public JottTokenizationException(Cause cause, JottTokenizerContext ctx) {
        super(JottTokenizationException.getMessage(cause, ctx));
    }
}
