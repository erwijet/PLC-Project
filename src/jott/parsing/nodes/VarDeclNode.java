package jott.parsing.nodes;

import jott.JottType;
import jott.SemanticException;
import jott.SymbolTable;
import jott.ValidationContext;
import jott.parsing.ParseContext;
import jott.tokenization.Token;
import jott.tokenization.TokenType;

public class VarDeclNode extends JottNode {
    TypeNode type;
    Token identifier;

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
    public void validateTree(ValidationContext ctx) {
        if(!Character.isLowerCase(identifier.getTokenString().charAt(0))) // not starting with lowercase letter
            throw new SemanticException(SemanticException.Cause.LOWERCASE_VARS, identifier);
        ctx.table.insert(new SymbolTable.Binding(identifier, identifier.getTokenString(), type.resolveType(ctx)));

    }
}
