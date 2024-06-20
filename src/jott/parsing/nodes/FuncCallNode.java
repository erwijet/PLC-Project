package jott.parsing.nodes;

import jott.parsing.ParseContext;
import jott.tokenization.Token;
import jott.tokenization.TokenType;

public class FuncCallNode extends JottNode {
    private Token name;
    private ParamsNode params;

    public static FuncCallNode parse(ParseContext ctx){
        // < func_call > -> :: < id >[ < params >]
        FuncCallNode node = new FuncCallNode();
        ctx.eat(TokenType.FC_HEADER);
        node.name = ctx.eat(TokenType.ID_KEYWORD);
        ctx.eat(TokenType.L_BRACE);
        node.params = ParamsNode.parse(ctx);
        ctx.eat(TokenType.R_BRACE);
        return node;
    }

    @Override
    public String convertToJott() {
        return "::" + name.getToken() + "[" + params.convertToJott() + "]";
    }
}
