package jott.parsing;

import jott.tokenization.Token;
import jott.tokenization.TokenType;

import java.util.List;
import java.util.Objects;

public class ParseContext {

    private List<Token> tokens;
    private int curIdx;

    public ParseContext(List<Token> tokens) {
        this.tokens = tokens;
        this.curIdx = 0;
    }

    private Token peekNext() {
        if (curIdx < tokens.size())
            return tokens.get(curIdx);
        else return null;
    }

    public String peekNextStr() {
        var next = peekNext();
        return next == null ? null : next.getToken();
    }

    public TokenType peekNextType() {
        var next = peekNext();
        return next == null ? null : next.getTokenType();
    }

    public boolean peekIs(TokenType type, String str) {
        return peekNextType() == type && Objects.equals(peekNextStr(), str);
    }

    public Token eat(TokenType expectedType) {
        Token curToken = peekNext();
        if (curToken == null)
            throw new RuntimeException("SyntaxError: Unexpected EOF, expected: " + expectedType);

        if (curToken.getTokenType() == expectedType) {
            curIdx++;
            return curToken;
        } else {
            throw new RuntimeException("SyntaxError: Expected " + expectedType + " but found " + curToken.getTokenType()
                + " (at line " + curToken.getLineNum() + ")");
        }
    }

    public Token eat(TokenType expectedType, String expectedStr) {
        Token token = eat(expectedType);
        if (!Objects.equals(token.getToken(), expectedStr)) {
            throw new RuntimeException("Syntax error: Expected " + expectedStr + " but found " + expectedStr);
        }

        return token;
    }
}
