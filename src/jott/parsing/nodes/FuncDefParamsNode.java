package jott.parsing.nodes;

import jott.JottType;
import jott.SymbolTable;
import jott.ValidationContext;
import jott.parsing.ParseContext;
import jott.tokenization.Token;
import jott.tokenization.TokenType;

import java.util.LinkedList;
import java.util.List;

public class FuncDefParamsNode extends JottNode {
    Token paramName;
    TypeNode paramType;
    List<FuncDefParamsTNode> tail;
    boolean isEmpty = true;

    static FuncDefParamsNode parse(ParseContext ctx) {
        // < func_def_params > -> <id >: < type > < function_def_params_t >* | empty
        FuncDefParamsNode node = new FuncDefParamsNode();
        if(ctx.peekNextType() == TokenType.ID_KEYWORD) {
            node.isEmpty = false;
            node.paramName = ctx.eat(TokenType.ID_KEYWORD);
            ctx.eat(TokenType.COLON);
            node.paramType = TypeNode.parse(ctx);
            node.tail = new LinkedList<>();

            while (ctx.peekNextType() == TokenType.COMMA) {
                node.tail.add(FuncDefParamsTNode.parse(ctx));
            }
        }
        return node;

    }

    @Override
    public String convertToJott() {
        if (!isEmpty) {
            StringBuilder str = new StringBuilder(paramName.getTokenString() + ": " + paramType.convertToJott());
            for (FuncDefParamsTNode t: tail) str.append(t.convertToJott());
            return str.toString();
        }

        return "";
    }

    public List<JottType> resolveParamTypes(ValidationContext ctx) {
        List<JottType> paramTypes = new LinkedList<>(List.of(paramType.resolveType(ctx)));
        tail.forEach(node -> paramTypes.add(node.resolveType(ctx)));

        return paramTypes;
    }

    @Override
    public void validateTree(ValidationContext ctx) {
        SymbolTable.Binding binding = new SymbolTable.Binding(paramName,
                paramName.getTokenString(),
                paramType.resolveType(ctx));
        ctx.table.insert(binding);
        tail.forEach(node -> node.validateTree(ctx));
    }
}
