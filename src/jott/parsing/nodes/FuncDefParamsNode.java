package jott.parsing.nodes;

import jott.parsing.ParseContext;
import jott.tokenization.Token;
import jott.tokenization.TokenType;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class FuncDefParamsNode extends JottNode {
    Token paramName;
    TypeNode paramType;
    List<FuncDefParamsTNode> tail;
    Boolean empty;

    static FuncDefParamsNode parse(ParseContext ctx) {
        // < func_def_params > -> <id >: < type > < function_def_params_t >* | empty
        FuncDefParamsNode node = new FuncDefParamsNode();
        if(ctx.peekNextType() == TokenType.ID_KEYWORD) {
            node.empty = Boolean.FALSE;
            node.paramName = ctx.eat(TokenType.ID_KEYWORD);
            ctx.eat(TokenType.COLON);
            node.paramType = TypeNode.parse(ctx);
            node.tail = new LinkedList<>();

            while (ctx.peekNextType() == TokenType.COMMA) {
                node.tail.add(FuncDefParamsTNode.parse(ctx));
            }
        }
        node.empty = Boolean.TRUE;
        return node;

    }

    @Override
    public String convertToJott() {
        if(!empty){
            String str = paramName.getToken() + ": " + paramType.convertToJott();
            for(FuncDefParamsTNode t: tail) str += t.convertToJott();
            return str;
        }
        return "";
    }
}
