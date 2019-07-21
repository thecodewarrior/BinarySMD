package dev.thecodewarrior.binarysmd.tokenizer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class Token {
    public final @NotNull String value;
    public final @Nullable String whitespaceBefore, whitespaceAfter;
    public final int line, column;

    public Token(@Nullable String whitespaceBefore, @NotNull String value, @Nullable String whitespaceAfter, int line, int column) {
        this.value = value;
        this.line = line;
        this.column = column;
        this.whitespaceBefore = whitespaceBefore;
        this.whitespaceAfter = whitespaceAfter;
    }

    public Token(@NotNull String value, int line, int column) {
        this.value = value;
        this.line = line;
        this.column = column;
        this.whitespaceBefore = null;
        this.whitespaceAfter = null;
    }

    @Override
    public String toString() {
        return value;
    }

    @NotNull
    public String toQuotedString() {
        if(value.length() < 2 || value.charAt(0) != '"' || value.charAt(value.length()-1) != '"')
            throw new ParseException(this, "`" + value + "` is not a double quoted string");
        return value.substring(1, value.length()-1);
    }

    public int toInt() {
        try {
            return Integer.parseInt(value);
        } catch(NumberFormatException e) {
            throw new ParseException(this, e);
        }
    }

    public float toFloat() {
        try {
            return Float.parseFloat(value);
        } catch(NumberFormatException e) {
            throw new ParseException(this, e);
        }
    }

    public void expectLine() {
        expect("\n");
    }

    public void expect(@NotNull String token) {
        if(!test(token)) throw new ParseException(this, "Expected `" + token + "`, found `" + value + "`");
    }

    public boolean testLine() {
        return test("\n");
    }

    public boolean test(@NotNull String token) {
        return token.equals(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Token)) return false;
        Token token = (Token) o;
        return line == token.line &&
                column == token.column &&
                value.equals(token.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, line, column);
    }
}
