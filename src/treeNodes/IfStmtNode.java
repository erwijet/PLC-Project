package treeNodes;

import provided.JottType;
import provided.SemanticException;
import provided.SemanticValidationContext;
import provided.ParseContext;
import provided.TokenType;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class IfStmtNode extends JottNode {
    private ExprNode condition;
    private BodyNode body;
    private List<ElseIfNode> elseif;
    private ElseNode els;

    public static IfStmtNode parse(ParseContext ctx){
        // < if_stmt > -> If [ < expr >]{ < body >} < elseif_lst >*< else >
        IfStmtNode node = new IfStmtNode(); // not actually sure if making a node is needed?
        node.elseif = new LinkedList<>();
        ctx.eat(TokenType.ID_KEYWORD, "If");
        ctx.eat(TokenType.L_BRACKET);
        node.condition = ExprNode.parse(ctx);
        ctx.eat(TokenType.R_BRACKET);
        ctx.eat(TokenType.L_BRACE);
        node.body = BodyNode.parse(ctx);
        ctx.eat(TokenType.R_BRACE);
        while(Objects.equals(ctx.peekNextStr(), "Elseif")) // if elseif exists, add them
            node.elseif.add(ElseIfNode.parse(ctx));
        node.els = ElseNode.parse(ctx); // else
        return node;
    }

    public boolean hasReturn() {
        return body.hasReturn()
                && elseif.stream().allMatch(ElseIfNode::hasReturn)
                && (els == null || els.hasReturn());
    }

    @Override
    public String convertToJott() {
        String str = "If[" + condition.convertToJott() + "]{\n" + body.convertToJott() + "\n}";
        for(ElseIfNode elseif : elseif) str += elseif.convertToJott();
        if(els != null) str += els.convertToJott();
        return str;
    }

    @Override
    public String convertToC() {
        String str = "if(" + condition.convertToC() + "){\n" + body.convertToC() + "\n}";
        for(ElseIfNode elseif : elseif) str += elseif.convertToC();
        if(els != null) str += els.convertToC();
        return str;
    }

    @Override
    public String convertToJava(String className) {
        String str = "if(" + condition.convertToJava(className) + "){\n" + body.convertToJava(className) + "\n}";
        for(ElseIfNode elseif : elseif) str += elseif.convertToJava(className);
        if(els != null) str += els.convertToJava(className);
        return str;
    }

    @Override
    public String convertToPython() {
        return "if " + condition.convertToPython() + ":\n"
                + body.convertToPython().lines().map(each -> "\t" + each).collect(Collectors.joining("\n"))
                + elseif.stream().map(it -> "\n" + it.convertToPython()).collect(Collectors.joining("\n"))
                + Optional.of(els).map(it -> "\n" + it.convertToPython()).orElse("");
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

        elseif.forEach(node -> node.validateTree(ctx));
        if (els != null)
            els.validateTree(ctx);
    }
}
