package treeNodes;

import provided.JottType;
import provided.SemanticException;
import provided.SymbolTable;
import provided.SemanticValidationContext;
import provided.ParseContext;
import provided.Token;
import provided.TokenType;

import java.util.List;
import java.util.stream.Collectors;

public class FuncCallNode extends JottNode {
    private Token name;
    private ParamsNode params;

    public static boolean canParse(ParseContext ctx) {
        return ctx.peekNextType() == TokenType.FC_HEADER;
    }

    public static FuncCallNode parse(ParseContext ctx){
        // < func_call > -> :: < id >[ < params >]
        FuncCallNode node = new FuncCallNode();
        ctx.eat(TokenType.FC_HEADER);
        node.name = ctx.eat(TokenType.ID_KEYWORD);
        ctx.eat(TokenType.L_BRACKET);
        node.params = ParamsNode.parse(ctx);
        ctx.eat(TokenType.R_BRACKET);
        return node;
    }

    @Override
    public String convertToJott() {
        return "::" + name.getTokenString() + "[" + params.convertToJott() + "]";
    }

    // todo: need to do print, length, concat for all 3 languages
    @Override
    public String convertToC() {
        if(name.getTokenString().equalsIgnoreCase("print")){
            String str = "printf(";
            // this is where the % stuff goes
            str += ", " + params.convertToC() + ")";
            return str;
        }
        if(name.getTokenString().equalsIgnoreCase("concat")){

        }
        if(name.getTokenString().equalsIgnoreCase("length")){
            return "strlen(" + params.convertToC() + ")";
        }
        return name.getTokenString() + "(" + params.convertToC() + ")";
    }

    @Override
    public String convertToJava(String className) {
        if(name.getTokenString().equalsIgnoreCase("print")) {
            String str = "System.out.println(" + params.expr.convertToJava(className);
            for(int i = 0; i < params.tail.size(); i++) {
                int index = params.tail.get(i).convertToJava(className).indexOf(", ");
                str += " + " + params.tail.get(i).convertToJava(className).substring(index);
            }
            return str + ")";
        }
        if(name.getTokenString().equalsIgnoreCase("concat")) {
            String str = params.expr.convertToJava(className);
            for(int i = 0; i < params.tail.size(); i++) {
                int index = params.tail.get(i).convertToJava(className).indexOf(", ");
                str += " + " + params.tail.get(i).convertToJava(className).substring(index);
            }
            return str;
        }
        if(name.getTokenString().equalsIgnoreCase("length")) {
            return params.convertToJava(className) + ".length()";
        }
        return name.getTokenString() + "(" + params.convertToJava(className) + ")";
    }

    @Override
    public String convertToPython() {
        if(name.getTokenString().equalsIgnoreCase("concat")) {
            return params.expr.convertToPython()
                    + params.tail.stream().map(each -> "+" + each.expr.convertToPython()).collect(Collectors.joining());
        }

        if(name.getTokenString().equalsIgnoreCase("length")) {
            return "len(" + params.convertToPython() + ")";
        }

        return name.getTokenString() + "(" + params.convertToC() + ")"; // this works for print by default, i think
    }

    public Token getToken() {
        return name;
    }

    @Override
    public void validateTree(SemanticValidationContext ctx) {
        SymbolTable.Function fn = ctx.table.resolve(name.getTokenString(), SymbolTable.Function.class)
                .orElseThrow(() -> new SemanticException(SemanticException.Cause.UNKNOWN_FUNCTION, name));

        List<JottType> paramTypes = params.resolveParameterTypes(ctx);
        if (paramTypes.size() != fn.parameterTypes.size())
            throw new SemanticException(SemanticException.Cause.INCORRECT_ARGUMENT_COUNT,
                    name,
                    fn.parameterTypes.size(),
                    paramTypes.size());

        for (int i = 0; i < paramTypes.size(); i++) {
            JottType expected = fn.parameterTypes.get(i);
            JottType found = paramTypes.get(i);

            if (expected == JottType.ANY)
                continue;

            if (expected != found)
                throw new SemanticException(SemanticException.Cause.TYPE_CONFLICT, name, expected, found);
        }
    }
}
