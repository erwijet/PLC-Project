package jott.parsing.nodes;

import jott.parsing.ParseContext;
import jott.tokenization.Token;
import jott.tokenization.TokenType;

public class FuncDefParamsTNode extends JottNode {
    Token funcName;
    TypeNode returnType;

    static FuncDefParamsTNode parse(ParseContext ctx) {
        // < func_def_params_t > -> ,<id >: < type >
        FuncDefParamsTNode node = new FuncDefParamsTNode();

        ctx.eat(TokenType.COMMA);
        node.funcName = ctx.eat(TokenType.ID_KEYWORD);
        ctx.eat(TokenType.COLON);
        node.returnType = TypeNode.parse(ctx);

        return node;
    }

    @Override
    public String convertToJott() {
        return ", " + funcName.getToken() + ":" + returnType.convertToJott();
    }
}
