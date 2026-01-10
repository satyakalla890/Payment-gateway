package com.gateway.util;

import java.time.YearMonth;
import java.util.Random;
import java.util.regex.Pattern;

public class PaymentUtils {

    private static final Pattern VPA_PATTERN =
            Pattern.compile("^[a-zA-Z0-9._-]+@[a-zA-Z0-9]+$");

    public static boolean isValidVpa(String vpa) {
        return vpa != null && VPA_PATTERN.matcher(vpa).matches();
    }

    public static boolean isValidCardNumber(String number) {
        if (number == null) return false;
        number = number.replaceAll("[\\s-]", "");
        if (!number.matches("\\d{13,19}")) return false;

        int sum = 0;
        boolean alternate = false;
        for (int i = number.length() - 1; i >= 0; i--) {
            int n = number.charAt(i) - '0';
            if (alternate) {
                n *= 2;
                if (n > 9) n -= 9;
            }
            sum += n;
            alternate = !alternate;
        }
        return sum % 10 == 0;
    }

    public static String detectCardNetwork(String number) {
        number = number.replaceAll("[\\s-]", "");
        if (number.startsWith("4")) return "visa";
        if (number.matches("^5[1-5].*")) return "mastercard";
        if (number.startsWith("34") || number.startsWith("37")) return "amex";
        if (number.startsWith("60") || number.startsWith("65") || number.matches("^8[1-9].*"))
            return "rupay";
        return "unknown";
    }

    public static boolean isExpiryValid(String month, String year) {
        int m = Integer.parseInt(month);
        int y = year.length() == 2 ? 2000 + Integer.parseInt(year) : Integer.parseInt(year);
        YearMonth expiry = YearMonth.of(y, m);
        return !expiry.isBefore(YearMonth.now());
    }

    public static boolean randomSuccess(int percent) {
        return new Random().nextInt(100) < percent;
    }
}
