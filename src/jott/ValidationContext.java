package jott;

import java.util.Optional;

public class ValidationContext {
    public SymbolTable table;
    private String enclosingFunctionName;

    public ValidationContext() {
        this.table = new SymbolTable();
    }

    /**
     * Sets the current function name
     * @param name the name of a function symbol existing in the current scope
     */
    public void enterFunction(String name) {
        enclosingFunctionName = name;
    }

    /**
     * Clears the current function name
     */
    public void exitFunction() {
        enclosingFunctionName = null;
    }

    public Optional<SymbolTable.Function> getEnclosingFunction() {
        return Optional.ofNullable(enclosingFunctionName).map(name ->
                this.table.resolve(name, SymbolTable.Function.class).orElseThrow(() ->
                        // the enclosing function, if set, should always exist in our symbol table
                        new SemanticException(SemanticException.Cause.MALFORMED_TREE, null)));
    }
}
