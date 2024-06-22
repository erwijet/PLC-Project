package jott.parsing.nodes;

import jott.JottTree;
import jott.parsing.ParseContext;
import jott.tokenization.TokenType;

import java.util.List;

public class BodyStmtNode extends JottNode {
    JottNode stmt;

    public static boolean canParse(ParseContext ctx) {
        return ctx.peekNextStr() != null && !ctx.peekNextStr().equals("Return") && ((ctx.peekNextType() == TokenType.ID_KEYWORD && List.of("If", "While").contains(ctx.peekNextStr()))
            || AssignmentNode.canParse(ctx)
            || FuncCallNode.canParse(ctx));
    }

    public static BodyStmtNode parse(ParseContext ctx) {
        BodyStmtNode node = new BodyStmtNode();
        // < body_stmt > -> < if_stmt > | < while_loop > | < asmt > | < func_call >;

        if (ctx.peekNextType() == TokenType.ID_KEYWORD) {
            if (ctx.peekNextStr().equals("If")) {
                node.stmt = IfStmtNode.parse(ctx);
                return node;
            }

            if (ctx.peekNextStr().equals("While")) {
                node.stmt = WhileLoopNode.parse(ctx);
                return node;
            }
        }

        if (AssignmentNode.canParse(ctx)) {
            node.stmt = AssignmentNode.parse(ctx);
            return node;
        }

        if (FuncCallNode.canParse(ctx)) {
            node.stmt = FuncCallNode.parse(ctx);
            ctx.eat(TokenType.SEMICOLON);
            return node;
        }

        throw new RuntimeException(
                "SyntaxError: Expected If block, While block, Assignment, or Function Call. Found: " + ctx.peekNextStr());
    }

    @Override
    public String convertToJott() {
        var ret = new StringBuilder();
        ret.append(stmt.convertToJott());

        if (stmt instanceof FuncCallNode) {
            ret.append(";");
        }

        return ret.toString();
    }
}
