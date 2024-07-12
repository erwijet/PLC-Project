package treeNodes;

import provided.JottTree;
import provided.SemanticValidationContext;

abstract class JottNode implements JottTree {
    private JottNode[] children;

    // note: as these methods need to be implemented in later phases
    // we should move them to be defined in each subclass of JottNode, rather than
    // just a blanket stub implementation here in the base class

    @Override
    public String convertToC() {
        return null; // stub
    }

    @Override
    public String convertToJava(String className) {
        return null; // stub
    }

    @Override
    public String convertToPython() {
        return null; // stub
    }

    @Override
    public void validateTree(SemanticValidationContext ctx) {
        // pass
    }

    @Override
    public String toString() {
        return this.convertToJott();
    }
}