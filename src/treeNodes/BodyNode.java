package treeNodes;

import provided.SemanticValidationContext;
import provided.ParseContext;

import java.util.LinkedList;
import java.util.List;

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
    public void validateTree(SemanticValidationContext ctx) {
        bodyStmts.forEach(stmt -> stmt.validateTree(ctx));

        if (returnStmt != null)
            returnStmt.validateTree(ctx);
    }
}
