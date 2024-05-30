package jott.tokenization;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.util.*;

public class JottTokenizerContext {
    final private String filename;
    public int lineNumber;
    public StringBuilder buffer;
    public ArrayList<Token> tokens;
    private Queue<Character> chars;

    JottTokenizerContext(String filename) {
        this.filename = filename;
        this.lineNumber = 1;
        this.buffer = new StringBuilder();
        this.tokens = new ArrayList<>();

        this.chars = new LinkedList<>(JottTokenizerContext.readCharactersFromFile(filename));
        this.chars.add('\0'); // allow for any nodes to have one final pass to commit their tokens
    }

    private static Collection<Character> readCharactersFromFile(String filename) {
        ArrayList<Character> chars = new ArrayList<>();
        File f = new File(filename);

        try (Scanner scanner = new Scanner(f).useDelimiter("(\\b|\\B)")) {
            while (scanner.hasNext()) {
                String chr = scanner.next();
                chars.add(chr.charAt(0));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return chars;
    }

    public boolean isDone() {
        return this.chars.isEmpty();
    }

    public void commit(TokenType tokenType) {
        Token token = new Token(buffer.toString(), filename, lineNumber, tokenType);
        tokens.add(token);
        buffer.setLength(0);
    }

    public void consume() {
        Character chr = this.chars.poll();
        if (chr == null) return;

        if (chr == '\n') {
            this.lineNumber++;
        }

        this.buffer.append(chr);
    }

    public Character peekNext() {
        return this.chars.peek();
    }
}
