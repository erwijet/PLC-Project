package jott.parsing.nodes;

import jott.parsing.ParseContext;
import jott.tokenization.Token;
import jott.tokenization.TokenType;

import java.util.Objects;

public class OperandNode extends JottNode {
    private Token id;
    private Token num;
    private FuncCallNode func;
    private Boolean negative;

    public static boolean canParse(ParseContext ctx) {
        return ctx.peekNextType() == TokenType.ID_KEYWORD
                || ctx.peekNextType() == TokenType.NUMBER
                || ctx.peekNextType() == TokenType.FC_HEADER
                || (ctx.peekNextType() == TokenType.MATH_OP && Objects.equals(ctx.peekNextStr(), "-"));
    }

    public static OperandNode parse(ParseContext ctx) {
        // < operand > -> <id > | <num > | < func_call > | -< num >
        OperandNode node = new OperandNode();
        node.negative = Boolean.FALSE;
        if(ctx.peekNextType() == TokenType.ID_KEYWORD)
            node.id = ctx.eat(TokenType.ID_KEYWORD);
        else if (ctx.peekNextType() == TokenType.NUMBER)
            node.num = ctx.eat(TokenType.NUMBER);
        else if(ctx.peekNextType() == TokenType.FC_HEADER)
            node.func = FuncCallNode.parse(ctx);
        else {
            ctx.eat(TokenType.MATH_OP, "-");
            node.num = ctx.eat(TokenType.NUMBER);
            node.negative = Boolean.TRUE;
        }
        return node;
    }

    @Override
    public String convertToJott() {
        if(id != null)
            return id.getToken();
        if(func != null)
            return func.convertToJott();
        String str = "";
        if(negative)
            str += "-";
        str += num.getToken();
        return str;
    }
}
