package com.victorpalha.fadesppayment.payments.entities.payment.helpers;

import com.victorpalha.fadesppayment.payments.entities.payment.helpers.LuhnValidation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LuhnValidationTest {
    @Test
    public void testValidCardNumber() {
        assertTrue(LuhnValidation.execute("4539578763621486")); // Visa valid
    }

    @Test
    public void testInvalidCardNumber() {
        assertFalse(LuhnValidation.execute("1234567890123456"));
    }
}
