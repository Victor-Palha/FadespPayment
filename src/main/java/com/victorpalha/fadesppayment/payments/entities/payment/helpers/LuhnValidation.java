package com.victorpalha.fadesppayment.payments.entities.payment.helpers;

public class LuhnValidation {
    public static boolean execute(String cardNumber) {
        int sum = 0;
        boolean shouldDouble = false;

        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(cardNumber.charAt(i));

            if (shouldDouble) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }

            sum += digit;
            shouldDouble = !shouldDouble;
        }

        return sum % 10 == 0;
    }
}
