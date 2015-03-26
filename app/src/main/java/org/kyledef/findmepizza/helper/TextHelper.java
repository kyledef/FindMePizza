package org.kyledef.findmepizza.helper;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

public class TextHelper {
    public static String formatUserText(String input){
        input = input.trim(); // Remove leading and trailing whitespaces
        return WordUtils.capitalizeFully(input);
    }

    public static String replaceNthWord(String word, int n, String insert){
        String [] wordArr = StringUtils.split(word);
        wordArr[n] = insert;
        return StringUtils.join(wordArr, " ");
    }

    public static String convertPrice(String cost){
        String [] wordArr = StringUtils.split(cost);
        if (wordArr.length % 2 == 0){
            for (int i = 0; i < wordArr.length; i++){
                if (i % 2 != 0){
                    wordArr[i] = "$" + wordArr[i] + ".00 TTD";
                    //TODO Accommodate the possibility that price may contain cents
                }
            }
        }

        return StringUtils.join(wordArr, " ");
    }
}
