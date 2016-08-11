package com.google.engedu.ghosttwo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class FastDictionary implements GhostDictionary {

    private TrieNode root;
    private static final int MIN_WORD_LENGTH = 4;

    public FastDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        root = new TrieNode();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
                root.add(line.trim());
        }
    }
    @Override
    public boolean isWord(String word) {
        return root.isWord(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        return root.getAnyWordStartingWith(root, prefix);
    }

    @Override
    public String getGoodWordStartingWith(String prefix, int whoWentFirst) {
        return root.getGoodWordStartingWith(root, prefix, whoWentFirst);
    }
}