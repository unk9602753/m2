package com.epam.esm.validator;

import org.springframework.stereotype.Component;

@Component
public class NumberValidator {
    private static final String DOUBLE_REGEX = "\\d+.\\d+";
    private static final String INTEGER_REGEX = "\\d+";

    public boolean isPositiveDouble(String str) {
        if (str == null || !str.matches(DOUBLE_REGEX)) {
            return false;
        }
        double num;
        try {
            num = Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return num > 0;
    }

    public boolean isPositiveInteger(String str) {
        if (str == null || !str.matches(INTEGER_REGEX)) {
            return false;
        }
        int num;
        try {
            num = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return num > 0;
    }
}
