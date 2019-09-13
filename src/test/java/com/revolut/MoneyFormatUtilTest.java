package com.revolut;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class MoneyFormatUtilTest {

    @Test
    public void shouldFormatAmountToString() {
        BigDecimal amount1 = BigDecimal.valueOf(10.0909);
        assertEquals("10.09", MoneyFormatUtil.format(amount1));

        BigDecimal amount2 = BigDecimal.valueOf(0.1);
        assertEquals("0.10", MoneyFormatUtil.format(amount2));
    }

    @Test
    public void shouldParseStringToDoubleAmount() {
        assertEquals(BigDecimal.valueOf(10.109), MoneyFormatUtil.parse("10.10923", 3));

        assertEquals(BigDecimal.valueOf(90.11), MoneyFormatUtil.parse("90.11", 2));
    }
}