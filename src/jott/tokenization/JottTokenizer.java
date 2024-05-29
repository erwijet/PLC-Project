package jott.tokenization; /**
 * This class is responsible for tokenizing Jott code.
 * 
 * @author 
 **/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class JottTokenizer {
	enum DFANode {
		START,
		HASH,
		COMMA,
		RBRACKET,
		LBRACKET,
		RBRACE,
		LBRACE,
		ASSIGN,
		ASSIGN_RELOP,
		RELOP,
		MATHOP,
		SEMICOLON,
		DIGIT,
		DIGIT_NUMBER,
		NUMBER,
		ID_OR_KEYWORD,
		COLON,
		COLON_FCHEADER,
		BANG,
		BANG_RELOP,
		QUOTE,
		QUOTE_STR
	}

	private static ArrayList<Character> readCharactersFromFile(String filename) {
		ArrayList<Character> chars = new ArrayList<>();
		File f = new File(filename);

		try (Scanner scanner = new Scanner(f)) {
			while (scanner.hasNext()) {
				String chr = scanner.next();
				chars.add(chr.charAt(0));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return chars;
	}

	/**
     * Takes in a filename and tokenizes that file into Tokens
     * based on the rules of the Jott Language
     * @param filename the name of the file to tokenize; can be relative or absolute path
     * @return an ArrayList of Jott Tokens
     */
    public static ArrayList<Token> tokenize(String filename) {
		List<Character> chrs = JottTokenizer.readCharactersFromFile(filename);

		Stream.of(chrs).forEach(System.out::println);

		
	}
}