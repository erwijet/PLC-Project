package treeNodes;

import provided.JottType;
import provided.SemanticException;
import provided.SemanticValidationContext;
import provided.ParseContext;
import provided.TokenType;

public class WhileLoopNode extends JottNode {
    private ExprNode condition;
    private BodyNode body;

    public static WhileLoopNode parse(ParseContext ctx){
        // < while_loop > -> While [ < expr >]{ < body >}
        WhileLoopNode node = new WhileLoopNode(); // not actually sure if making a node is needed?
        ctx.eat(TokenType.ID_KEYWORD, "While");
        ctx.eat(TokenType.L_BRACKET);
        node.condition = ExprNode.parse(ctx);
        ctx.eat(TokenType.R_BRACKET);
        ctx.eat(TokenType.L_BRACE);
        node.body = BodyNode.parse(ctx);
        ctx.eat(TokenType.R_BRACE);
        return node;
    }

    @Override
    public String convertToJott() {
        return "While[" + condition.convertToJott() + "]{\n" + body.convertToJott() + "\n}";
    }

    @Override
    public String convertToC() {
        return "while(" + condition.convertToC() + "){\n" + body.convertToC() + "\n}";
    }

    @Override
    public String convertToJava(String className) {
        return "while(" + condition.convertToJava(className) + "){\n" + body.convertToJava(className) + "\n}";
    }

    @Override
    public String convertToPython() {
        return "while " + condition.convertToPython() + ":\n\t" + body.convertToPython() + "\n";
    }

    @Override
    public void validateTree(SemanticValidationContext ctx) {
        JottType conditionType = condition.resolveType(ctx);
        if (conditionType != JottType.BOOLEAN) { // make sure condition is a boolean
            throw new SemanticException(SemanticException.Cause.TYPE_CONFLICT,
                    condition.getFirstToken(),
                    JottType.BOOLEAN,
                    conditionType);
        }

        condition.validateTree(ctx);
        ctx.table.pushScope();
        body.validateTree(ctx);
        ctx.table.popScope();
    }
}
