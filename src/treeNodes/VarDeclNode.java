package treeNodes;

import provided.SemanticException;
import provided.SymbolTable;
import provided.SemanticValidationContext;
import provided.ParseContext;
import provided.Token;
import provided.TokenType;

import java.util.LinkedList;
import java.util.List;

public class VarDeclNode extends JottNode {
    TypeNode type;
    Token identifier;
    private LinkedList<String> keywords = new LinkedList<>(List.of(new String[]{"while", "if", "else", "elseif", "def"}));

    public static boolean canParse(ParseContext ctx) {
        return TypeNode.canParse(ctx);
    }

    public static VarDeclNode parse(ParseContext ctx) {
        // < var_dec > -> < type > < id >;
        VarDeclNode node = new VarDeclNode();
        node.type = TypeNode.parse(ctx);
        node.identifier = ctx.eat(TokenType.ID_KEYWORD);
        ctx.eat(TokenType.SEMICOLON);

        return node;
    }

    @Override
    public String convertToJott() {
        return type.convertToJott() + " " + identifier.getTokenString() + ";";
    }

    @Override
    public String convertToC() {
        return type.convertToC() + " " + identifier.getTokenString() + ";";
    }

    @Override
    public String convertToJava(String className) {
        return type.convertToJava(className) + " " + identifier.getTokenString() + ";";
    }

    @Override
    public String convertToPython() {
        return identifier.getTokenString();
    }

    @Override
    public void validateTree(SemanticValidationContext ctx) {
        for (String keyword : keywords)
            if (identifier.getTokenString().equalsIgnoreCase(keyword))
                throw new SemanticException(SemanticException.Cause.KEYWORD, identifier);
        if(!Character.isLowerCase(identifier.getTokenString().charAt(0))) // not starting with lowercase letter
            throw new SemanticException(SemanticException.Cause.LOWERCASE_VARS, identifier);
        ctx.table.insert(new SymbolTable.Binding(identifier, identifier.getTokenString(), type.resolveType(ctx)));

    }
}
