package jott;

import jott.tokenization.Token;

import java.util.*;

public class SymbolTable {
    public abstract static class Symbol {
        public String name;
        public Token token;

        public Symbol(Token token, String name) {
            this.token = token;
            this.name = name;
        }
    }

    public static class Binding extends Symbol {
        public JottType type;

        public Binding(Token token, String name, JottType type) {
            super(token, name);
            this.type = type;
        }
    }

    public static class Function extends Symbol {
        public List<JottType> parameterTypes;
        public JottType returnType;

        public boolean isVoid() {
            return returnType == null;
        }

        public Function(String name, List<JottType> parameterTypes, JottType returnType) {
            super(null, name);
            this.parameterTypes = parameterTypes;
            this.returnType = returnType;
        }

        public Function(Token token, List<JottType> parameterTypes, JottType returnType) {
            super(token, token.getTokenString());
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

    private static Map<String, Symbol> builtins = Map.of(
            "print", new Function("print", List.of(JottType.ANY), null),
            "concat", new Function("concat", List.of(JottType.STRING, JottType.STRING), JottType.STRING)
    );

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
        Symbol builtin = builtins.get(symbolName);
        if (builtin != null)
            return Optional.of(builtin);

        for (Object map : scopes.toArray()) {
            Symbol symbol = ((Map<String, Symbol>)map).get(symbolName);
            if (symbol != null)
                return Optional.of(symbol);
        }

        return Optional.empty();
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
     * @throws SemanticException if a symbol with a conflicting {@link Symbol#name name} exists
     */
    public <T extends Symbol> void insert(T symbol) {
        Optional<Symbol> conflicting = resolve(symbol.name);
        if (conflicting.isPresent()) {
            throw new SemanticException(SemanticException.Cause.CONFLICTING_IDENTIFIER,
                    conflicting.get().token,
                    symbol.name,
                    null);
        }

        this.getScope().put(symbol.name, symbol);
    }
}
