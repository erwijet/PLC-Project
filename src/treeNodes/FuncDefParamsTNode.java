package treeNodes;

import provided.JottType;
import provided.SymbolTable;
import provided.SemanticValidationContext;
import provided.ParseContext;
import provided.Token;
import provided.TokenType;

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

    @Override
    public String convertToC() {
        return ", " + paramType.convertToC() + " " + paramName.getTokenString();
    }

    @Override
    public String convertToJava(String className) {
        return ", " + paramType.convertToJava(className) + " " + paramName.getTokenString();
    }

    @Override
    public String convertToPython() {
        return ", " + paramType.convertToPython() + " " + paramName.getTokenString();
    }

    public JottType resolveType(SemanticValidationContext ctx) {
        return paramType.resolveType(ctx);
    }

    @Override
    public void validateTree(SemanticValidationContext ctx) {
        SymbolTable.Binding binding = new SymbolTable.Binding(paramName,
                paramName.getTokenString(),
                paramType.resolveType(ctx));
        ctx.table.insert(binding);
    }
}
