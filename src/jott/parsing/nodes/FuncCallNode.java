package jott.parsing.nodes;

import jott.JottType;
import jott.SemanticException;
import jott.SymbolTable;
import jott.ValidationContext;
import jott.parsing.ParseContext;
import jott.tokenization.Token;
import jott.tokenization.TokenType;

import java.util.List;

public class FuncCallNode extends JottNode {
    private Token name;
    private ParamsNode params;

    public static boolean canParse(ParseContext ctx) {
        return ctx.peekNextType() == TokenType.FC_HEADER;
    }

    public static FuncCallNode parse(ParseContext ctx){
        // < func_call > -> :: < id >[ < params >]
        FuncCallNode node = new FuncCallNode();
        ctx.eat(TokenType.FC_HEADER);
        node.name = ctx.eat(TokenType.ID_KEYWORD);
        ctx.eat(TokenType.L_BRACKET);
        node.params = ParamsNode.parse(ctx);
        ctx.eat(TokenType.R_BRACKET);
        return node;
    }

    @Override
    public String convertToJott() {
        return "::" + name.getTokenString() + "[" + params.convertToJott() + "]";
    }

    public Token getToken() {
        return name;
    }

    @Override
    public void validateTree(ValidationContext ctx) {
        SymbolTable.Function fn = ctx.table.resolve(name.getTokenString(), SymbolTable.Function.class)
                .orElseThrow(() -> new SemanticException(SemanticException.Cause.UNKNOWN_FUNCTION, name));

        List<JottType> paramTypes = params.resolveParameterTypes(ctx);
        if (paramTypes.size() != fn.parameterTypes.size())
            throw new SemanticException(SemanticException.Cause.INCORRECT_ARGUMENT_COUNT,
                    name,
                    fn.parameterTypes.size(),
                    paramTypes.size());

        for (int i = 0; i < paramTypes.size(); i++) {
            JottType expected = fn.parameterTypes.get(i);
            JottType found = paramTypes.get(i);

            if (expected != found)
                throw new SemanticException(SemanticException.Cause.TYPE_CONFLICT, name, expected, found);
        }
    }
}
