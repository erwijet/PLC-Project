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

    public Token peekNext() {
        return tokens.get(curIdx);
    }

    public String peekNextStr() {
        return peekNext().getToken();
    }

    public TokenType peekNextType() {
        return peekNext().getTokenType();
    }

    public Token eat(TokenType expectedType) {
        Token curToken = peekNext();
        if (curToken.getTokenType() == expectedType) {
            curIdx++;
            return curToken;
        } else {
            throw new RuntimeException("Syntax error: Expected " + expectedType + " but found " + curToken.getTokenType());
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
