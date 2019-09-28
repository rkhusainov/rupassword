package com.khusainov.rinat.rupasswords;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PasswordsHelperTest {

    private static final String[] RUS = {"й", "ц", "у", "к", "е", "н"};
    private static final String[] ENG = {"q", "w", "e", "r", "t", "y"};

    public static final String[] SOURCES = {
            "",
            "некуцй",
            "ЙЦУКЕН",
            "зщшгне"
    };

    public static final String[] RESULTS = {
            "",
            "ytrewq",
            "QWERTY",
            "зщшгне"
    };


    private PasswordsHelper mHelper;

    @Before
    public void setUp() throws Exception {
        mHelper = new PasswordsHelper(RUS, ENG);
    }

    @Test
    public void testConvert() {
        assertEquals("Error in test case", SOURCES.length == RESULTS.length);
        for (int i = 0; i < SOURCES.length; i++) {
            assertEquals(RESULTS[i], mHelper.convert(SOURCES[i]));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void convertIsThrows() {

    }
}