package com.khusainov.rinat.rupasswords;

import java.util.regex.Pattern;

public class PasswordsHelper {
    private final String[] russians;
    private final String[] latins;

    public PasswordsHelper(String[] russians, String[] latins) {
        this.russians = russians;
        this.latins = latins;

        if (russians.length != latins.length) {
            throw new IllegalArgumentException();
        }
    }


    public String convert(CharSequence source) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            String s = String.valueOf(c);

            boolean found = false;


            for (int j = 0; j < russians.length; j++) {
                if (russians[j].equals(s) || russians[j].toUpperCase().equals(s)) {
                    result.append(Character.isUpperCase(c) ?
                            latins[j].toUpperCase() : latins[j]);
                    found = true;
                    break;
                }
            }

            if (!found) {
                result.append(c);
            }
        }
        return result.toString();
    }

    public int passwordLevel(String password) {

        int level = 0;

        String dig = "(?=.*[0-9]).{1,}";
        String symb = "(?=.*[a-z]).{1,}";
        String upsymb = "(?=.*[A-Z]).{1,}";
        String spec = "(?=.*[@#$%*^&+=|<>?!{}()\\[\\]~])(?=\\S+$).{1,}";
        String digSymb = "(?=.*[0-9])(?=.*[a-z]).{1,}";
        String digSymbUpsymb = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{1,}";
        String digSymbUpsymbSpec = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%*^&+=|<>?!{}()\\[\\]~])(?=\\S+$).{1,}";
        String hard = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%*^&+=|<>?!{}()\\[\\]~])(?=\\S+$).{8,}";


        if (password.matches(dig)) {
            level = 2000;
        }

        if (password.matches(symb)) {
            level = 2000;
        }

        if (password.matches(upsymb)) {
            level = 2000;
        }


        if (password.matches(spec)) {
            level = 2000;
        }

        if (password.matches(digSymb)) {
            level = 4000;
        }

        if (password.matches(dig) && password.matches(upsymb)) {
            level = 4000;
        }

        if (password.matches(dig) && password.matches(spec)) {
            level = 4000;
        }

        if (password.matches(symb) && password.matches(upsymb)) {
            level = 4000;
        }

        if (password.matches(symb) && password.matches(spec)) {
            level = 4000;
        }

        if (password.matches(upsymb) && password.matches(spec)) {
            level = 4000;
        }

        if (password.matches(digSymbUpsymb)) {
            level = 6000;
        }

        if (password.matches(digSymb) & password.matches(spec)) {
            level = 6000;
        }

        if (password.matches(dig) && password.matches(upsymb) && password.matches(spec)) {
            level = 6000;
        }


        if (password.matches(digSymbUpsymbSpec)) {
            level = 8000;
        }

        if (password.matches(hard)) {
            level = 10000;
        }


        return level;
    }

}
