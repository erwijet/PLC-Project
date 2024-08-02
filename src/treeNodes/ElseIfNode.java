package treeNodes;

import provided.JottType;
import provided.SemanticException;
import provided.SemanticValidationContext;
import provided.ParseContext;
import provided.TokenType;

import java.util.stream.Collectors;

public class ElseIfNode extends JottNode {
    ExprNode condition;
    BodyNode body;

    public static ElseIfNode parse(ParseContext ctx){
        // < elseif > -> Elseif [ < expr >]{ < body >}
        ElseIfNode node = new ElseIfNode(); // not actually sure if making a node is needed?
        ctx.eat(TokenType.ID_KEYWORD, "Elseif");
        ctx.eat(TokenType.L_BRACKET);
        node.condition = ExprNode.parse(ctx);
        ctx.eat(TokenType.R_BRACKET);
        ctx.eat(TokenType.L_BRACE);
        node.body = BodyNode.parse(ctx);
        ctx.eat(TokenType.R_BRACE);
        return node;
    }

    public boolean hasReturn() {
        return body.hasReturn();
    }

    @Override
    public String convertToJott() {
        return "Elseif[" + condition.convertToJott() + "]{\n" + body.convertToJott() + "\n}";
    }
    @Override
    public String convertToC() {
        return "else if(" + condition.convertToC() + "){\n" + body.convertToC() + "\n}";
    }

    @Override
    public String convertToJava(String className) {
        return "else if(" + condition.convertToJava(className) + "){\n" + body.convertToC() + "\n}";
    }

    @Override
    public String convertToPython() {
        return "else if " + condition.convertToPython() + ":\n"
                + body.convertToPython().lines().map(line -> "\t" + line).collect(Collectors.joining("\n"));
    }

    @Override
    public void validateTree(SemanticValidationContext ctx) {
        JottType conditionType = condition.resolveType(ctx);

        if (conditionType != JottType.BOOLEAN) {
            throw new SemanticException(SemanticException.Cause.TYPE_CONFLICT,
                    condition.getFirstToken(),
                    JottType.BOOLEAN,
                    conditionType);
        }

        ctx.table.pushScope();
        body.validateTree(ctx);
        ctx.table.popScope();
    }
}
