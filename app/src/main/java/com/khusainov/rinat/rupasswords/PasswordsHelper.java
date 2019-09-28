package com.khusainov.rinat.rupasswords;

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
                if (russians[j].equals(s)) {
                    result.append(latins[j]);
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

}
