package utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

public class RandomUtils {

    public static int randomInt(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }

    public static String randomString(int length) {
        return RandomStringUtils.randomAlphabetic(length);
    }

    public static String randomPhoneNumber() {
        Random random = new Random();

        int num1 = random.nextInt(7) + 1;
        int num2 = random.nextInt(8);
        int num3 = random.nextInt(8);

        int set2 = random.nextInt(643) + 100;
        int set3 = random.nextInt(8999) + 1000;

        return "+7 (" + num1 + num2 + num3 + ")" + "-" + set2 + "-" + set3;
    }

    public static String randomDate(int startYear, int endYear) {
        int day = randomInt(1, 28);
        int month = randomInt(1, 12);
        int year = randomInt(startYear, endYear);

        return year + "-" + month + "-" + day;
    }
}