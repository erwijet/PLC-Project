package jott.tokenization;

public class JottTokenizationException extends Exception {
    public enum Cause {
        UNEXPECTED_CHARACTER,
        OPEN_SOURCE_FILE
    }

    private static String buildMessage(Cause cause, JottTokenizerContext ctx) {
        switch (cause) {
            case UNEXPECTED_CHARACTER -> {
                Character chr = ctx.peekNext();
                String current = chr == null || chr == '\0' ? "EOF" : chr.toString();

                return String.format("Syntax Error:\nUnexpected character '%s' on line %s\n%s:%s", current, ctx.lineNumber, ctx.filename, ctx.lineNumber);
            }
            case OPEN_SOURCE_FILE -> {
                return String.format("Failed to open the requested source file: '%s'", ctx.filename);
            }
        }

        return "Unknown cause";
    }

    public JottTokenizationException(Cause cause, JottTokenizerContext ctx) {
        super(JottTokenizationException.buildMessage(cause, ctx));
    }
}
