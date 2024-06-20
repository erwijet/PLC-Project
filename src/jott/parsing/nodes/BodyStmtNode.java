package jott.parsing.nodes;

import jott.JottTree;
import jott.parsing.ParseContext;

public class BodyStmtNode extends JottNode {
    public static BodyStmtNode parse(ParseContext ctx) {
        // < body_stmt > -> < if_stmt > | < while_loop > | < asmt > | < func_call >;
        return null; // for now
    }

    @Override
    public String convertToJott() {
        return ""; // for now
    }
}
