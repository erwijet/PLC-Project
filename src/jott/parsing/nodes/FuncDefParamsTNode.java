package jott.parsing.nodes;

import jott.JottType;
import jott.ValidationContext;
import jott.parsing.ParseContext;
import jott.tokenization.Token;
import jott.tokenization.TokenType;

public class FuncDefParamsTNode extends JottNode {
    Token paramName;
    TypeNode paramType;

    static FuncDefParamsTNode parse(ParseContext ctx) {
        // < func_def_params_t > -> ,<id >: < type >
        FuncDefParamsTNode node = new FuncDefParamsTNode();

        ctx.eat(TokenType.COMMA);
        node.paramName = ctx.eat(TokenType.ID_KEYWORD);
        ctx.eat(TokenType.COLON);
        node.paramType = TypeNode.parse(ctx);

        return node;
    }

    @Override
    public String convertToJott() {
        return ", " + paramName.getTokenString() + ":" + paramType.convertToJott();
    }

    public JottType resolveType(ValidationContext ctx) {
        return paramType.resolveType(ctx);
    }
}
