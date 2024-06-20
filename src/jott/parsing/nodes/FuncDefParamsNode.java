package jott.parsing.nodes;

import jott.parsing.ParseContext;
import jott.tokenization.Token;
import jott.tokenization.TokenType;

import java.util.LinkedList;
import java.util.List;

public class FuncDefParamsNode extends JottNode {
    Token paramName;
    TypeNode paramType;
    List<FuncDefParamsTNode> tail;

    static FuncDefParamsNode parse(ParseContext ctx) {
        FuncDefParamsNode node = new FuncDefParamsNode();

        node.paramName = ctx.eat(TokenType.ID_KEYWORD);
        ctx.eat(TokenType.COLON);
        node.paramType = TypeNode.parse(ctx);
        node.tail = new LinkedList<>();

        while (ctx.peekNextType() == TokenType.COMMA) {
            node.tail.add(FuncDefParamsTNode.parse(ctx));
        }

        return node;
    }

    @Override
    public String convertToJott() {
        return null;
    }
}
