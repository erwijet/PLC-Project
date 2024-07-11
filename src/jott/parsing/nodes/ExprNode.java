package jott.parsing.nodes;

import jott.JottTree;
import jott.JottType;
import jott.parsing.ParseContext;
import jott.parsing.ParseException;
import jott.tokenization.TokenType;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ExprNode extends JottNode {
    private enum Variant {
        LITERAL,
        OPERAND,
        MATH_OP,
        REL_OP
    }

    List<JottNode> children;
    Variant variant;

    public static boolean canParse(ParseContext ctx) {
        return OperandNode.canParse(ctx)
            || ctx.peekNextType() == TokenType.NUMBER
            || ctx.peekNextType() == TokenType.STRING;
    }

    public static ExprNode parse(ParseContext ctx) {
        // <expr> -> <operand> | <operand><relop><operand> | <operand><mathop><operand> | <str_lit> | <bool>
        ExprNode node = new ExprNode();
        node.children = new LinkedList<>();

        if (ctx.peekNextType() == TokenType.ID_KEYWORD &&
                (ctx.peekNextStr().equals("True") || ctx.peekNextStr().equals("False"))) {
            // handle <bool>
            node.variant = Variant.LITERAL;
            node.children.add(BoolNode.parse(ctx));
            return node;
        } else if (OperandNode.canParse(ctx)) {
            // handle <operand>

            node.variant = Variant.OPERAND;
            node.children.add(OperandNode.parse(ctx));

            if (ctx.peekNextType() == TokenType.REL_OP) {
                // handle <operand><relop><operand>

                node.variant = Variant.REL_OP;
                node.children.add(RelopNode.parse(ctx));
                node.children.add(OperandNode.parse(ctx));
            } else if (ctx.peekNextType() == TokenType.MATH_OP) {
                // handle <operand><mathop><operand>

                node.variant = Variant.MATH_OP;
                node.children.add(MathOpNode.parse(ctx));
                node.children.add(OperandNode.parse(ctx));
            }
            return node;
        } else if (ctx.peekNextType() == TokenType.STRING) {
            // handle <str_lit>
            node.variant = Variant.LITERAL;
            node.children.add(StrLiteralNode.parse(ctx));
            return node;
        }

        throw new ParseException(ParseException.Cause.SYNTAX, ctx, "identifier, operator, function call, or literal");
    }

    @Override
    public String convertToJott() {
        return children
                .stream()
                .map(JottTree::convertToJott)
                .collect(Collectors.joining());
    }

    public JottType resolveType() {
        return switch (variant) {
            case LITERAL -> {
                if (children.get(0) instanceof StrLiteralNode) yield JottType.STRING;
                if (children.get(0) instanceof BoolNode) yield JottType.BOOLEAN;
                throw new RuntimeException("unexpected node type: children@0");
            }
            case REL_OP -> {
                JottNode node = children.get(1);
                if (node instanceof RelopNode)
                    yield ((RelopNode) node).resolveType();

                throw new RuntimeException("unexpected node type: children@1");
            }
            case MATH_OP -> {

            }

        };

    }
}
