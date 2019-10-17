package com.khusainov.rinat.rupasswords;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class PasswordsHelper {

    private static final int MIN_UNIQUE_SYMBOLS = 4;

    private final String[] russians;
    private final String[] latins;

    public static final char[] SYMBOLS = {'@', '#', '_', '%', '&'};

    private Random random;

    /**
     * Конструктор принимает словари русских и соответствующих им латинских символов
     *
     * @param russians русские символы
     * @param latins   латинские символы
     * @throws IllegalArgumentException если длины словарей не совпадают
     */

    public PasswordsHelper(String[] russians, String[] latins) {
        this.russians = russians;
        this.latins = latins;

        if (russians.length != latins.length) {
            throw new IllegalArgumentException();
        }

        random = new Random();
    }

    /**
     * Преобразует строку с русским паролем в латинские символы. Регистр символов сохраняется,
     * отсутствующие в словаре символы остаются как есть.
     *
     * @param source исходный пароль
     * @return Преобразованный пароль
     */

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

        int digits = 0;
        int symbols = 0;
        int uppercases = 0;
        int letters = 0;

        // коллекция для подсчета уникальных символов
        Set<Character> uniques = new HashSet<>();

        // За каждые 4 символа начислим единицу силы пароля, но не более 3
        level += Math.min(3, password.length());

        // читаем пароль посимвольно и проверяем
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            uniques.add(c);

            if (Character.isLetterOrDigit(c)) {
                if (Character.isDigit(c)) {
                    digits++;
                } else {
                    letters++;
                }

                if (Character.isUpperCase(c)) {
                    uppercases++;
                }
            } else {
                symbols++;
            }
        }

        // определяем силу пароля
        level += digits > 0 ? Math.min(2, digits) : 0;
        level += uppercases > 0 ? Math.min(2, uppercases) : 0;
        level += symbols > 0 ? Math.min(2, symbols) : 0;

        level += uniques.size() - MIN_UNIQUE_SYMBOLS > 0 ? Math.min(2, uniques.size()) : 0;

        // Штраф за одни цифры и символы
        if (letters == 0) {
            level--;
        }

        // Штраф за один и тот же символ
        if (uniques.size() == 1) {
            level=1;
        }

        return Math.max(0, Math.min(10, level));
    }

    /**
     * Генерирует пароль с указанной сложностью
     *
     * @param length длина генерируемого пароля
     * @param caps использовать ли заглавные символы
     * @param digits использовать ли цифры
     * @param symbols использовать ли спецсимволы
     *
     * @return Сгенерированный пароль
     */
    public String generatePassword(int length, boolean caps, boolean digits, boolean symbols) {
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            boolean capitalize = caps && random.nextBoolean();
            boolean nonLetter = (digits || symbols) && random.nextBoolean();

            char c;

            if (nonLetter) {
                if ((digits && !symbols) || (digits && random.nextBoolean())) {
                    c = Character.forDigit(random.nextInt(9), 10);
                } else {
                    c = SYMBOLS[random.nextInt(10000) % SYMBOLS.length];
                }
            } else {
                int letter = random.nextInt(26);
                int code = 'a' + letter;
                c = Character.toChars(code)[0];
            }

            password.append(capitalize? Character.toUpperCase(c) :  c);
        }

        return password.toString();
    }
}
