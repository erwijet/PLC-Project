package jott.parsing.nodes;

import jott.JottTree;
import jott.parsing.ParseContext;
import jott.tokenization.Token;
import jott.tokenization.TokenType;

public class FuncDefNode extends JottNode {
    private Token funcId;
    private FuncDefParamsNode params;
    private FuncReturnNode returnType;
    private FuncBodyNode body;

    public static FuncDefNode parse(ParseContext ctx) {
        FuncDefNode node = new FuncDefNode();

        ctx.eat(TokenType.ID_KEYWORD, "Def");
        node.funcId = ctx.eat(TokenType.ID_KEYWORD);
        ctx.eat(TokenType.R_BRACKET);
        node

        while ()
    }

}
