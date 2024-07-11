package jott.parsing.nodes;

import jott.SymbolTable;
import jott.ValidationContext;
import jott.parsing.ParseContext;
import jott.tokenization.Token;
import jott.tokenization.TokenType;

public class FuncDefNode extends JottNode {
    private Token funcId;
    private FuncDefParamsNode params;
    private FuncReturnNode funcReturn;
    private FuncBodyNode body;

    public static FuncDefNode parse(ParseContext ctx) {
        // < function_def > -> Def <id >[ func_def_params ]: < function_return >{ < f_body >
        FuncDefNode node = new FuncDefNode();

        ctx.eat(TokenType.ID_KEYWORD, "Def");
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
}
