package dev.thecodewarrior.binarysmd.tokenizer;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SuppressWarnings("WeakerAccess")
public class Tokenizer {

    private int index = 0;
    private @NotNull List<Token> tokens = new ArrayList<>();

    public Tokenizer(@NotNull BufferedReader data) {
        Pattern tokenPattern = Pattern.compile("\"[^\"]*\"|\\S+");

        List<String> lines = data.lines().collect(Collectors.toList());
        for (int lineNum = 0; lineNum < lines.size(); lineNum++) {
            String line = lines.get(lineNum);
            Matcher matcher = tokenPattern.matcher(line);
            while(matcher.find()) {
                tokens.add(new Token(matcher.group(), lineNum, matcher.start()));
            }
            int end = line.indexOf('\r');
            if(end < 0) end = line.indexOf('\n');
            if(end < 0) end = line.length();
            tokens.add(new Token("\n", lineNum, end));
        }
    }

    public void reset() {
        index = 0;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        if(index < 0) {
            this.index = 0;
        } else if(index > tokens.size()) {
            this.index = tokens.size();
        } else {
            this.index = index;
        }
    }

    public Tokenizer back() {
        if(index < 0)
            throw new ParseException(tokens.get(0), "Beginning of file reached");
        setIndex(getIndex() - 1);
        return this;
    }

    public Tokenizer advance() {
        if(index >= tokens.size())
            throw new ParseException(tokens.get(tokens.size() - 1), "End of file reached");
        setIndex(getIndex() + 1);
        return this;
    }

    /**
     * Gets the token at the current index.
     * @return The token at the current index
     */
    @NotNull
    public Token current() {
        return tokens.get(index);
    }

    /**
     * Gets the token at the current index and advances to the next token.
     * @return The token at the current index
     */
    @NotNull
    public Token next() {
        if(index >= tokens.size())
            throw new ParseException(tokens.get(tokens.size() - 1), "End Of File reached");
        return tokens.get(index++);
    }

    /**
     * @return true if all tokens have been consumed
     */
    public boolean eof() {
        return index == tokens.size();
    }
}
