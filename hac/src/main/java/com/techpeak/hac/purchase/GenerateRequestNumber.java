package com.techpeak.hac.purchase;

import java.time.Year;

public class GenerateRequestNumber {
    private static final String DEFAULT_PREFIX = "REQ";
    private static final int NUMBER_PADDING_LENGTH = 4;

    public static String generateRequestNumber(String prefix, String lastNumber) {
        String effectivePrefix = (prefix != null && !prefix.isEmpty()) ? prefix : DEFAULT_PREFIX;
        int nextNumber = getNextNumber(lastNumber, effectivePrefix);
        return effectivePrefix + Year.now().getValue() + formatNumber(nextNumber);
    }

    private static int getNextNumber(String lastNumber, String prefix) {
        if (lastNumber == null || lastNumber.length() <= prefix.length() + NUMBER_PADDING_LENGTH) {
            return 1;
        } else {
            String yearPart = lastNumber.substring(prefix.length(), prefix.length() + NUMBER_PADDING_LENGTH);
            String numberPart = lastNumber.substring(prefix.length() + NUMBER_PADDING_LENGTH);
            int year = Integer.parseInt(yearPart);
            if (year == Year.now().getValue()) {
                return Integer.parseInt(numberPart) + 1;
            } else {
                return 1;
            }
        }
    }

    private static String formatNumber(int number) {
        return String.format("%0" + NUMBER_PADDING_LENGTH + "d", number);
    }
}
