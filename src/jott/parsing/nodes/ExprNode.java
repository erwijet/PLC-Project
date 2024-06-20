package jott.parsing.nodes;

import jott.parsing.ParseContext;
import jott.tokenization.TokenType;

import java.util.LinkedList;
import java.util.List;

public class ExprNode extends JottNode {
    List<JottNode> children;

    public static ExprNode parse(ParseContext ctx) {
        // <expr> -> <operand> | <operand><relop><operand> | <operand><mathop><operand> | <str_lit> | <bool>
        ExprNode node = new ExprNode();
        node.children = new LinkedList<>();

        if (ctx.peekNextType() == TokenType.ID_KEYWORD &&
                (ctx.peekNextStr().equals("True") || ctx.peekNextStr().equals("False"))) {
            // handle <bool>
            node.children.add(BoolNode.parse(ctx));
            return node;
        } else if (ctx.peekNextType() == TokenType.ID_KEYWORD || ctx.peekNextType() == TokenType.NUMBER) {
            // handle <operand>

            node.children.add(OperandNode.parse(ctx));

            if (ctx.peekNextType() == TokenType.REL_OP) {
                // handle <operand><relop><operand>
                node.children.add(RelopNode.parse(ctx));
                node.children.add(OperandNode.parse(ctx));
            } else if (ctx.peekNextType() == TokenType.MATH_OP) {
                // handle <operand><mathop><operand>
                node.children.add(MathOpNode.parse(ctx));
                node.children.add(OperandNode.parse(ctx));
            }
            return node;
        } else if (ctx.peekNextType() == TokenType.STRING) {
            // handle <str_lit>
            node.children.add(StrLiteralNode.parse(ctx));
            return node;
        }

        throw new RuntimeException("SyntaxError: unexpected token '" + ctx.peekNextStr() + "' in expression");
    }

    @Override
    public String convertToJott() {
        return null;
    }
}
