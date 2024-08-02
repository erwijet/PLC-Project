package treeNodes;

import provided.SemanticValidationContext;
import provided.ParseContext;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BodyNode extends JottNode {
    List<BodyStmtNode> bodyStmts;
    ReturnStmtNode returnStmt;

    public static BodyNode parse(ParseContext ctx) {
        // < body > -> < body_stmt >* < return_stmt >
        BodyNode node = new BodyNode();
        node.bodyStmts = new LinkedList<>();

        while (BodyStmtNode.canParse(ctx)) {
            node.bodyStmts.add(BodyStmtNode.parse(ctx));
        }

        node.returnStmt = ReturnStmtNode.parse(ctx);

        return node;
    }

    public boolean hasReturn() {
        return !returnStmt.isEmpty || bodyStmts.stream().anyMatch(BodyStmtNode::hasReturn);
    }

    @Override
    public String convertToJott() {
        String str = "";
        for (BodyStmtNode stmt : bodyStmts) str += stmt.convertToJott() + " ";
        return str + returnStmt.convertToJott();
    }

    @Override
    public String convertToC() {
        String str = "";
        for (BodyStmtNode stmt : bodyStmts) str += stmt.convertToC() + " ";
        return str + returnStmt.convertToC();
    }

    @Override
    public String convertToJava(String className) {
        String str = "";
        for (BodyStmtNode stmt : bodyStmts) str += stmt.convertToJava(className) + " ";
        return str + returnStmt.convertToJava(className);
    }

    @Override
    public String convertToPython() {
        return bodyStmts.stream().map(BodyStmtNode::convertToPython).collect(Collectors.joining("\n"))
            + Optional.of(returnStmt).map(it -> "\n" + it.convertToPython()).orElse("");
    }

    @Override
    public void validateTree(SemanticValidationContext ctx) {
        bodyStmts.forEach(stmt -> stmt.validateTree(ctx));

        if (returnStmt != null)
            returnStmt.validateTree(ctx);
    }
}
