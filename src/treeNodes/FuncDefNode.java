package treeNodes;

import provided.SemanticException;
import provided.SymbolTable;
import provided.SemanticValidationContext;
import provided.ParseContext;
import provided.Token;
import provided.TokenType;

import java.util.stream.Collectors;

public class FuncDefNode extends JottNode {
    public Token funcId;
    private FuncDefParamsNode params;
    private FuncReturnNode funcReturn;
    private FuncBodyNode body;

    public static FuncDefNode parse(ParseContext ctx) {
        // < function_def > -> Def <id >[ func_def_params ]: < function_return >{ < f_body >
        FuncDefNode node = new FuncDefNode();

        ctx.eatAndIgnoreCase(TokenType.ID_KEYWORD, "Def");
        node.funcId = ctx.eat(TokenType.ID_KEYWORD);
        ctx.eat(TokenType.L_BRACKET);
        node.params = FuncDefParamsNode.parse(ctx);
        ctx.eat(TokenType.R_BRACKET);
        ctx.eat(TokenType.COLON);
        node.funcReturn = FuncReturnNode.parse(ctx);
        ctx.eat(TokenType.L_BRACE);
        node.body = FuncBodyNode.parse(ctx);
        ctx.eat(TokenType.R_BRACE);

        return node;
    }

    @Override
    public String convertToJott() {
        return "Def " + funcId.getTokenString() + "[" + params.convertToJott() + "]: " + funcReturn.convertToJott() + "{\n" + body.convertToJott() + "\n}";
    }

    @Override
    public String convertToC() {
        if(funcId.getTokenString().equalsIgnoreCase("main"))
            return "int " + funcId.getTokenString() + "(void){" + body.convertToC() + "\nreturn 1;\n}";
        return funcReturn.convertToC() + " " + funcId.getTokenString() + "(" + params.convertToC() + "){" + body.convertToC() + "\n}";
    }

    @Override
    public String convertToJava(String className) {
        if(funcId.getTokenString().equalsIgnoreCase("main"))
            return "public static " + funcReturn.convertToJava(className) + " " + funcId.getTokenString() + "(String[] args){" + body.convertToJava(className) + "\n}";
        return "public static " + funcReturn.convertToJava(className) + " " + funcId.getTokenString() + "(" + params.convertToJava(className) + "){" + body.convertToJava(className) + "\n}";
    }

    @Override
    public String convertToPython() {
        return "def " + funcId.getTokenString() + "(" + params.convertToPython() + "):\n"
                + body.convertToPython().lines().map(line -> "\t" + line).collect(Collectors.joining("\n"));
    }

    @Override
    public void validateTree(SemanticValidationContext ctx) {
        if(funcId.getTokenString().equalsIgnoreCase("main"))
            if(!params.isEmpty || !funcReturn.isVoid()) // not correctly configured
                throw new SemanticException(SemanticException.Cause.MISSING_MAIN, funcId);
        SymbolTable.Function symbol = new SymbolTable.Function(
                funcId,
                params.resolveParamTypes(ctx),
                funcReturn.isVoid() ? null : funcReturn.type.resolveType(ctx));
        ctx.table.insert(symbol);

        ctx.table.pushScope();
        ctx.enterFunction(funcId.getTokenString());

        params.validateTree(ctx);
        body.validateTree(ctx);

        ctx.exitFunction();
        ctx.table.popScope();
    }
}
