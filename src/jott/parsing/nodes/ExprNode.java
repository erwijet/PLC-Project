package jott.parsing.nodes;

import jott.JottTree;
import jott.JottType;
import jott.SemanticException;
import jott.ValidationContext;
import jott.parsing.ParseContext;
import jott.parsing.ParseException;
import jott.tokenization.Token;
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

    public Token getFirstToken() {
        return switch (variant) {
            case LITERAL -> {
                JottNode node = children.get(0);
                if (node instanceof StrLiteralNode) yield ((StrLiteralNode) node).token;
                if (node instanceof BoolNode) yield ((BoolNode) node).token;
                throw new SemanticException(SemanticException.Cause.MALFORMED_TREE, null);
            }
            case REL_OP, MATH_OP, OPERAND -> ((OperandNode) children.get(0)).getToken();
        };
    }

    public JottType resolveType(ValidationContext ctx) {
        return switch (variant) {
            case LITERAL -> {
                if (children.get(0) instanceof StrLiteralNode) yield JottType.STRING;
                if (children.get(0) instanceof BoolNode) yield JottType.BOOLEAN;
                throw new SemanticException(SemanticException.Cause.MALFORMED_TREE, null);
            }
            case REL_OP -> JottType.BOOLEAN;
            case OPERAND -> ((OperandNode) children.get(0)).resolveType(ctx);
            case MATH_OP -> // lhs of math op sets type (either INTEGER/DOUBLE)
                    ((OperandNode) children.get(0)).resolveType(ctx);
        };
    }

    @Override
    public void validateTree(ValidationContext ctx) {
        resolveType(ctx);
    }
}
