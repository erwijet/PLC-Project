package jott;

import java.util.*;

public class SymbolTable {
    public class ConflictException extends Exception {
        public ConflictException(String msg) {
            super(msg);
        }
    }

    public abstract class Symbol {
        public String name;

        public Symbol(String name) {
            this.name = name;
        }
    }

    public class Binding extends Symbol {
        JottType type;

        public Binding(String name, JottType type) {
            super(name);
            this.type = type;
        }
    }

    public class Function extends Symbol {
        List<JottType> parameterTypes;
        JottType returnType;

        boolean isVoid() {
            return returnType == null;
        }

        public Function(String name, List<JottType> parameterTypes, JottType returnType) {
            super(name);
            this.parameterTypes = parameterTypes;
            this.returnType = returnType;
        }
    }

    //

    private Stack<Map<String, Symbol>> scopes;

    private Map<String, Symbol> getScope() {
        return scopes.peek();
    }

    public SymbolTable() {
        this.scopes = new Stack<>();
        pushScope();
    }

    //

    /**
     * Pushes a clean scope onto the scope stack
     */
    public void pushScope() {
        this.scopes.push(new HashMap<>());
    }

    /**
     * Drops the current scope frame, removing all tracked symbols
     * that were inserted since the last call to {@link SymbolTable#popScope()}
     */
    public void popScope() {
        this.scopes.pop();
    }

    /**
     * Attempts to look up a {@link Symbol symbol} by its {@link Symbol#name name} in the current scope.
     *
     * @param symbolName the {@link Symbol#name name} to search by
     * @return the {@link Symbol symbol}, if present
     */
    public Optional<Symbol> resolve(String symbolName) {
        return Optional.ofNullable(this.scopes.peek().get(symbolName));
    }

    /**
     * Attempts to look up a symbol by a {@link Symbol#name name} and concrete subtype of {@link Symbol}.
     *
     * @param symbolName the name to search by
     * @param clazz the subtype of {@link Symbol} to check against
     * @return the concrete symbol, if present
     */
    public <T extends Symbol> Optional<T> resolve(String symbolName, Class<T> clazz) {
        return resolve(symbolName)
                .filter(clazz::isInstance)
                .map(clazz::cast);
    }

    /**
     * Inserts a new symbol into the current scope
     *
     * @param symbol the symbol to insert
     * @throws ConflictException if a symbol with a conflicting {@link Symbol#name name} exists
     */
    public <T extends Symbol> void insert(T symbol) throws ConflictException {
        Optional<Symbol> conflicting = resolve(symbol.name);
        if (conflicting.isPresent()) {
            throw new ConflictException(String.format("Symbol '%s' (%s) already exists in the current scope.",
                    symbol.name,
                    conflicting.get()));
        }

        this.getScope().put(symbol.name, symbol);
    }
}
