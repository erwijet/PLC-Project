package jott.tokenization; /**
 * This class is responsible for tokenizing Jott code.
 * 
 * @author 
 **/

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Array;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
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
		NUMBER_NUMBER,
		NUMBER,
		ID_OR_KEYWORD,
		COLON,
		COLON_FCHEADER,
		BANG,
		BANG_RELOP,
		QUOTE,
		QUOTE_STR
	}

	/**
     * Takes in a filename and tokenizes that file into Tokens
     * based on the rules of the Jott Language
     * @param filename the name of the file to tokenize; can be relative or absolute path
     * @return an ArrayList of Jott Tokens
     */
    public static ArrayList<Token> tokenize(String filename) {
		DFANode state = DFANode.START;

		try {
			JottTokenizerContext ctx = new JottTokenizerContext(filename);

			while (!ctx.isDone()) {
				Character chr = ctx.peekNext();

				switch (state) {
					case START -> {
						ctx.buffer.setLength(0);

						if (Character.isWhitespace(chr)) {
							ctx.consume();
							continue;
						}

						if (chr == '#') {
							ctx.consume();
							state = DFANode.HASH;
						}

						if (chr == ',') {
							ctx.consume();
							state = DFANode.COMMA;
						}

						if (chr == ']') {
							ctx.consume();
							state = DFANode.RBRACKET;
						}

						if (chr == '[') {
							ctx.consume();
							state = DFANode.LBRACKET;
						}

						if (chr == '}') {
							ctx.consume();
							state = DFANode.RBRACE;
						}

						if (chr == '{') {
							ctx.consume();
							state = DFANode.LBRACE;
						}

						if (chr == '=') {
							ctx.consume();
							state = DFANode.ASSIGN;
						}

						if (List.of('<', '>').contains(chr)) {
							ctx.consume();
							state = DFANode.RELOP;
						}

						if (List.of('/', '+', '-', '*').contains(chr)) {
							ctx.consume();
							state = DFANode.MATHOP;
						}

						if (chr == ';') {
							ctx.consume();
							state = DFANode.SEMICOLON;
						}

						if (chr == '.') {
							ctx.consume();
							state = DFANode.DIGIT;
						}

						if (Character.isDigit(chr)) {
							ctx.consume();
							state = DFANode.NUMBER;
						}

						if (Character.isLetter(chr)) {
							ctx.consume();
							state = DFANode.ID_OR_KEYWORD;
						}

						if (chr == ':') {
							ctx.consume();
							state = DFANode.COLON;
						}

						if (chr == '!') {
							ctx.consume();
							state = DFANode.BANG;
						}

						if (chr == '"') {
							ctx.consume();
							state = DFANode.QUOTE;
						}

						if (chr == '\0') {
							ctx.consume(); // end of stream. eat and ignore
							continue;
						}

						if (state == DFANode.START) { // none were matched
							throw new JottTokenizationException(
									JottTokenizationException.Cause.UNEXPECTED_CHARACTER,
									ctx
							);
						}

					}

					case HASH -> {
						if (chr == '\n') {
							state = DFANode.START;
						}

						ctx.consume();
					}

					case COMMA -> {
						ctx.commit(TokenType.COMMA);
						state = DFANode.START;
					}

					case RBRACKET -> {
						ctx.commit(TokenType.R_BRACKET);
						state = DFANode.START;
					}

					case LBRACKET -> {
						ctx.commit(TokenType.L_BRACKET);
						state = DFANode.START;
					}

					case RBRACE -> {
						ctx.commit(TokenType.R_BRACE);
						state = DFANode.START;
					}

					case LBRACE -> {
						ctx.commit(TokenType.L_BRACE);
						state = DFANode.START;
					}

					case ASSIGN -> {
						if (chr == '=') {
							ctx.consume();
							state = DFANode.ASSIGN_RELOP;
						} else {
							ctx.commit(TokenType.ASSIGN);
							state = DFANode.START;
						}
					}

					case ASSIGN_RELOP, BANG_RELOP -> {
						ctx.commit(TokenType.REL_OP);
						state = DFANode.START;
					}

					case RELOP -> {
						if (chr == '=') {
							ctx.consume();
							state = DFANode.ASSIGN_RELOP;
						} else {
							ctx.commit(TokenType.REL_OP);
							state = DFANode.START;
						}
					}

					case MATHOP -> {
						ctx.commit(TokenType.MATH_OP);
						state = DFANode.START;
					}

					case SEMICOLON -> {
						ctx.commit(TokenType.SEMICOLON);
						state = DFANode.START;
					}

					case DIGIT -> {
						if (Character.isDigit(chr)) {
							ctx.consume();
							state = DFANode.NUMBER_NUMBER;
						} else {
							throw new JottTokenizationException(
									JottTokenizationException.Cause.UNEXPECTED_CHARACTER,
									ctx
							);
						}
					}

					case NUMBER -> {
						if (Character.isDigit(chr)) {
							ctx.consume();
						} else if (chr == '.') {
							ctx.consume();
							state = DFANode.NUMBER_NUMBER;
						} else {
							ctx.commit(TokenType.NUMBER);
							state = DFANode.START;
						}
					}

					case NUMBER_NUMBER -> {
						if (Character.isDigit(chr)) {
							ctx.consume();
						} else {
							ctx.commit(TokenType.NUMBER);
							state = DFANode.START;
						}
					}

					case ID_OR_KEYWORD -> {
						if (Character.isDigit(chr) || Character.isLetter(chr)) {
							ctx.consume();
						} else {
							ctx.commit(TokenType.ID_KEYWORD);
							state = DFANode.START;
						}
					}

					case COLON -> {
						if(chr.equals(":")){
							ctx.commit(TokenType.FC_HEADER);
							state = DFANode.START;
						} else {
							ctx.commit(TokenType.COLON);
							state = DFANode.START;
						}
					}

					case BANG -> {
						if(chr.equals("=")){
							ctx.commit(TokenType.NOT_EQUALS);
							state = DFANode.START;

						} else{
							throw new JottTokenizationException(
								JottTokenizationException.Cause.UNEXPECTED_CHARACTER,
									ctx
							);
						}
					}

					case QUOTE -> {
						if(Character.isDigit(chr) || Character.isLetter(chr) || Character.isSpaceChar(chr)){
							ctx.consume();
							state = DFANode.QUOTE;
						} else if(chr.equals('"')){
							ctx.commit(TokenType.STRING);
							state = DFANode.START;
						}
					}

					default -> {
						// TODO: remove this `default` branch once all cases are implemented
						System.err.println("Not Implemented");
						state = DFANode.START; // discard and start again
					}
				}
			}

			return ctx.tokens;
		} catch (JottTokenizationException exception) {
			System.err.println(exception.getMessage());
			return null;
		}
	}
}