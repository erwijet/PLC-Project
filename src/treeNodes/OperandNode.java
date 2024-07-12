package treeNodes;

import provided.JottType;
import provided.SemanticException;
import provided.SymbolTable;
import provided.SemanticValidationContext;
import provided.ParseContext;
import provided.Token;
import provided.TokenType;

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
            return id.getTokenString();
        if(func != null)
            return func.convertToJott();
        String str = "";
        if(negative)
            str += "-";
        str += num.getTokenString();
        return str;
    }

    public Token getToken() {
        if (id != null) return id;
        if (num != null) return num;
        if (func != null) return func.getToken();

        throw new SemanticException(SemanticException.Cause.MALFORMED_TREE, null);
    }

    public JottType resolveType(SemanticValidationContext ctx) {
        if (id != null) {
            return ctx.table.resolve(id.getTokenString(), SymbolTable.Binding.class)
                    .orElseThrow(() -> new SemanticException(SemanticException.Cause.UNKNOWN_BINDING, getToken()))
                    .type;
        }

        if (num != null) {
            if (num.getTokenString().contains(".")) return JottType.DOUBLE;
            else return JottType.INTEGER;
        }

        if (func != null) {
            return ctx.table.resolve(func.getToken().getTokenString(), SymbolTable.Function.class)
                    .orElseThrow(() -> new SemanticException(SemanticException.Cause.UNKNOWN_FUNCTION, getToken()))
                    .returnType;
        }

        throw new SemanticException(SemanticException.Cause.MALFORMED_TREE, null);
    }

    @Override
    public void validateTree(SemanticValidationContext ctx) {
        if (func != null)
            func.validateTree(ctx);

        resolveType(ctx);
    }
}
