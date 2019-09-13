package com.revolut;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class MoneyFormatUtil {

    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getCurrencyInstance();

    public static String format(BigDecimal amount) {
        DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) NUMBER_FORMAT).getDecimalFormatSymbols();
        decimalFormatSymbols.setCurrencySymbol("");
        ((DecimalFormat) NUMBER_FORMAT).setDecimalFormatSymbols(decimalFormatSymbols);
        NUMBER_FORMAT.setRoundingMode(RoundingMode.DOWN);
        return NUMBER_FORMAT.format(amount);
    }

    public static BigDecimal parse(String amount, int scale) {
        return new BigDecimal(amount).setScale(scale, RoundingMode.DOWN);
    }
}
