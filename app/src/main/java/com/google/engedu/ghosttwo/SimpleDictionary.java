package com.google.engedu.ghosttwo;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;
    private int mid;
    Random random=new Random();

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        //if prefix is empty select any random word
        if(prefix.equals("")){
            Random r=new Random();
            int val=r.nextInt(words.size());
            Log.v("",words.get(val));
            return words.get(val);
        }
        //else perform binary search
        else {
            String possibleWord = prefixBinarySearch(words, prefix);
            Log.v("Possible word is ",possibleWord);
            if (prefix.equals(possibleWord)) {
                return "sameAsPrefix";
            } else {
                return possibleWord;
            }
        }
    }

    // whoWentFirst = 0 - computer went first. Find words of even length
    // whoWentFirst = 1 - user went first. Find words of odd length
    @Override
    public String getGoodWordStartingWith(String prefix,int whoWentFirst) {
        //if prefix is empty select any random word
        if (prefix.equals("")) {
            Random r = new Random();
            int val = r.nextInt(words.size());
            Log.v("",words.get(val));
            return words.get(val);
        }
        else {
            String possibleWord = prefixBinarySearch(words, prefix);
            if (possibleWord.equals("noWord")){
                return possibleWord;
            }

            //we also need to check other words having same prefix
            else {
                ArrayList<String> wordsToConsider = new ArrayList<String>();
                int midCopy = mid;

                // Go upwards
                while (true) {
                    // Get word
                    String w = words.get(mid--);

                    // If the word does not contain prefix, we're done
                    int c = w.startsWith(prefix) ? 0 : prefix.compareTo(w);
                    if(c!=0)
                        break;

                    // If word's length is desirable, consider it
                    if (w.length() % 2 == whoWentFirst) {
                        wordsToConsider.add(w);
                    }
                }
                // Restore mid
                mid = midCopy;
                // Go downwards
                while (true) {
                    // Get word
                    String w = words.get(mid++);
                    // If the word does not contain prefix, we're done
                    int c = w.startsWith(prefix) ? 0 : prefix.compareTo(w);
                    if(c!=0)
                        break;

                    // If word's length is desirable, consider it
                    if (w.length() % 2 == whoWentFirst) {
                        wordsToConsider.add(w);
                    }
                }
                String selected;
                //if array list is empty
                if (wordsToConsider.size() == 0) {
                    selected = "noWord";
                }
                //select random word from the array list
                else {
                    selected = wordsToConsider.get(random.nextInt(wordsToConsider.size()));
                }

                //if selected word is same as prefix
                if(selected.equals(prefix))
                    return "sameAsPrefix";

                else
                    return selected;

            }
        }
    }

    //performing binary search and checking if word has required prefix
    public String prefixBinarySearch(ArrayList<String> arrayList, String prefix) {
        int first = 0;
        int last = arrayList.size() - 1;
        mid = 0;

        while (first <= last) {
            mid = (first + last) / 2;
            //get word at the mid position
            String s = arrayList.get(mid);

            //if word starts with the given prefix change value to 0
            int c = s.startsWith(prefix) ? 0 : prefix.compareTo(s);
            if (c > 0) {
                first = mid + 1;
            } else if (c == 0) {
                return arrayList.get(mid);
            } else
                last = mid - 1;
        }
        //if no words found starting with given prefix
        return "noWord";

    }
}
