package provided;

import provided.ParseException;
import provided.Token;
import provided.TokenType;

import java.util.List;
import java.util.Objects;

public class ParseContext {
    private List<Token> tokens;
    private int curIdx;

    public Token getTrailingToken() {
        return tokens.get(tokens.size() - 1);
    }

    public ParseContext(List<Token> tokens) {
        this.tokens = tokens;
        this.curIdx = 0;
    }

    public boolean isEOF() {
        return tokens.size() <= curIdx;
    }

    public Token peekNext() {
        if (isEOF()) return null;
        return tokens.get(curIdx);
    }
    public Token peekNextNext(){
        if (isEOF()) return null;
        return tokens.get(curIdx + 1);
    }

    public String peekNextStr() {
        if (isEOF()) return null;
        return peekNext().getTokenString();
    }
    public String peekNextNextStr(){
        if (isEOF()) return null;
        return peekNextNext().getTokenString();
    }

    public TokenType peekNextType() {
        if (isEOF()) return null;
        return peekNext().getTokenType();
    }

    public boolean peekIs(TokenType type, String str) {
        return peekNextType() == type && Objects.equals(peekNextStr(), str);
    }

    public Token eat(TokenType expectedType) {
        Token curToken = peekNext();
        if (curToken == null)
            throw new ParseException(ParseException.Cause.SYNTAX, this, expectedType);

        if (curToken.getTokenType() == expectedType) {
            curIdx++;
            return curToken;
        } else {
            if (expectedType == TokenType.ASSIGN && curToken.getLineNum() == 9) {
                System.out.println("pause");
            }

            throw new ParseException(ParseException.Cause.SYNTAX, this, expectedType);
        }
    }

    public Token eat(TokenType expectedType, String expectedStr) {
        Token token = eat(expectedType);
        if (!Objects.equals(token.getTokenString(), expectedStr)) {
            throw new ParseException(ParseException.Cause.SYNTAX, this, expectedStr);
        }

        return token;
    }

    public Token eatAndIgnoreCase(TokenType expectedType, String expectedStr) {
        Token token = eat(expectedType);
        if (!Objects.equals(token.getTokenString().toLowerCase(), expectedStr.toLowerCase())) {
            throw new ParseException(ParseException.Cause.SYNTAX, this, expectedStr);
        }

        return token;
    }
}
