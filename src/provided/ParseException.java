package provided;

public class ParseException extends RuntimeException {
    public enum Cause {
        SYNTAX,
        UNUSED_TOKENS
    }

    private static String buildMessage(Cause cause, ParseContext ctx, Object expected) {
        switch (cause) {
            case SYNTAX -> {
                if (ctx.isEOF()) return String.format("Syntax Error\nExpected %s but found EOF\n%s:%s", expected.toString(), ctx.getTrailingToken().getFilename(), ctx.getTrailingToken().getLineNum());
                return String.format("Syntax Error\nExpected %s but found %s '%s'\n%s:%s", expected.toString(), ctx.peekNextType(), ctx.peekNextStr(), ctx.peekNext().getFilename(), ctx.peekNext().getLineNum());
            }

            case UNUSED_TOKENS -> {
                return String.format("Syntax Error\nTop-level statements and expressions are not allowed.\n%s:%s", ctx.peekNext().getFilename(), ctx.peekNext().getLineNum());
            }
        }

        return "Unknown cause";
    }

    public ParseException(Cause cause, ParseContext ctx, Object expected) {
        super(buildMessage(cause, ctx, expected));
    }
}

