package dev.thecodewarrior.binarysmd.tokenizer;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class TokenPrinter {
    private @NotNull List<@NotNull List<@NotNull Token>> lines = new ArrayList<>();
    private String separator = " ";
    private int column = 0;

    public TokenPrinter() {
        lines.add(new ArrayList<>());
    }

    private List<Token> currentLine() {
        return lines.get(lines.size() - 1);
    }

    private void addToken(String token) {
        if(!currentLine().isEmpty())
            column += separator.length();
        currentLine().add(new Token(token, lines.size() - 1, column));
        column += token.length();
    }

    @NotNull
    public TokenPrinter print(String token) {
        if(token.matches(".*\\s.*")) {
            throw new IllegalArgumentException("Can't print a token containing whitespace");
        }
        addToken(token);
        return this;
    }

    @NotNull
    public TokenPrinter printString(String string) {
        addToken('"' + string + '"');
        return this;
    }

    @NotNull
    public TokenPrinter print(int number) {
        addToken("" + number);
        return this;
    }

    @NotNull
    public TokenPrinter print(float number) {
        addToken(String.format("%.6f", number));
        return this;
    }

    @NotNull
    public TokenPrinter newline() {
        lines.add(new ArrayList<>());
        column = 0;
        return this;
    }

    @NotNull
    public TokenPrinter print(Tokenizer tokenizer) {
        while(!tokenizer.eof()) {
            Token token = tokenizer.next();
            if(token.value.equals("\n"))
                newline();
            else
                addToken(token.value);
        }
        return this;
    }

    @Override
    public String toString() {
        StringBuilder value = new StringBuilder();
        for(List<Token> line : this.lines) {
            for(int i = 0; i < line.size(); i++) {
                value.append(line.get(i).value);
                if(i != line.size() - 1) {
                    value.append(separator);
                }
            }
            if(!line.isEmpty())
                value.append("\n");
        }
        return value.toString();
    }
}
