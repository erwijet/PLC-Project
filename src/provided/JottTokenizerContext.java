package provided;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class JottTokenizerContext {
    final public String filename;
    public int lineNumber;
    public StringBuilder buffer;
    public ArrayList<Token> tokens;
    private Queue<Character> chars;

    JottTokenizerContext(String filename) throws JottTokenizationException {
        this.filename = filename;
        this.lineNumber = 1;
        this.buffer = new StringBuilder();
        this.tokens = new ArrayList<>();

        try {
            this.chars = new LinkedList<>(JottTokenizerContext.readCharactersFromFile(filename));
            this.chars.add('\0'); // allow for any nodes to have one final pass to commit their tokens
        } catch (FileNotFoundException e) {
            throw new JottTokenizationException(
                    JottTokenizationException.Cause.OPEN_SOURCE_FILE,
                    this
            );
        }

    }

    private static Collection<Character> readCharactersFromFile(String filename) throws FileNotFoundException {
        ArrayList<Character> chars = new ArrayList<>();
        File f = new File(filename);

        try (Scanner scanner = new Scanner(f).useDelimiter("(\\b|\\B)")) {
            while (scanner.hasNext()) {
                String chr = scanner.next();
                chars.add(chr.charAt(0));
            }
        }

        return chars;
    }

    /**
     * @return true when characters should stop being read from the chars queue.
     */
    public boolean isDone() {
        return this.chars.isEmpty();
    }

    /**
     * Announces a new token from the tokenizer, then clears the buffer.
     *
     * @param tokenType The token type to announce
     */
    public void commit(TokenType tokenType) {
        Token token = new Token(buffer.toString(), filename, lineNumber, tokenType);
        tokens.add(token);
        buffer.setLength(0);
    }

    /**
     * "Eats" the next character, adding it to the buffer.
     * The next character can be peeked with {@link #peekNext()} method.
     */
    public void consume() {
        Character chr = this.chars.poll();
        if (chr == null) return;

        if (chr == '\n') {
            this.lineNumber++;
        }

        this.buffer.append(chr);
    }

    /**
     * Peeks the character that will be processed when {@link #consume()} is called.
     *
     * @return The next character to evaluate.
     */
    public Character peekNext() {
        return this.chars.peek();
    }
}
