package util;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public class Labels {

    public static String next(String label) {

        String nextCharSeqStr;
        char[] charSeqArr;
        boolean isResetAllChar = false;
        boolean isResetAfterIndex = false;
        Integer resetAfterIndex = 0;

        if (StringUtils.isBlank(label)) {
            charSeqArr = new char[]{'a'};
        } else {
            charSeqArr = label.toCharArray();
            Integer charSeqLen = charSeqArr.length;

            for (int index = charSeqLen - 1; index >= 0; index--) {
                char charAtIndex = charSeqArr[index];

                if (Character.getNumericValue(charAtIndex) % 35 == 0) {
                    if (index == 0) {
                        charSeqArr = Arrays.copyOf(charSeqArr, charSeqLen + 1);
                        isResetAllChar = true;
                    }
                } else {
                    char nextCharAtIndex = (char) (charAtIndex + 1);
                    charSeqArr[index] = nextCharAtIndex;
                    if (index + 1 < charSeqLen) {
                        isResetAfterIndex = true;
                        resetAfterIndex = index;
                    }
                    break;
                }
            }
            charSeqLen = charSeqArr.length;
            if (isResetAllChar) {
                for (int index = 0; index < charSeqLen; index++) {
                    charSeqArr[index] = 'a';
                }
            } else if (isResetAfterIndex) {
                for (int index = resetAfterIndex + 1; index < charSeqLen; index++) {
                    charSeqArr[index] = 'a';
                }
            }
        }

        nextCharSeqStr = String.valueOf(charSeqArr);
        return nextCharSeqStr;
    }
}
