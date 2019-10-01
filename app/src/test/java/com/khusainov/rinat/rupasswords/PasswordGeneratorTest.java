package com.khusainov.rinat.rupasswords;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PasswordGeneratorTest {
    private PasswordGenerator mGenerator;
    private int passwordLength;

    @Before
    public void setUp() throws Exception {
        passwordLength = 8;
    }

    @Test
    public void passwordGenerating() {
        mGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                .useLower(true)
                .useUpper(true)
                .useDigits(true)
                .useSpecSymbols(true)
                .build();

        String password = mGenerator.generate(passwordLength);

        assertEquals(8,password.length());
    }
}