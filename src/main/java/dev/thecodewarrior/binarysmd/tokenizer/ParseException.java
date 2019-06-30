package dev.thecodewarrior.binarysmd.tokenizer;

public class ParseException extends RuntimeException {
    public ParseException(Token token) {
        super(formatMessage("", token));
    }

    public ParseException(Token token, String message) {
        super(formatMessage(message, token));
    }

    public ParseException(Token token, String message, Throwable cause) {
        super(formatMessage(message, token), cause);
    }

    public ParseException(Token token, Throwable cause) {
        super(formatMessage("", token), cause);
    }

    public static String formatMessage(String message, Token token) {
        return String.format("%s at %d:%d", message, token.line, token.column);
    }
}
