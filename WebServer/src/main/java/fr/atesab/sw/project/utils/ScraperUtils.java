package fr.atesab.sw.project.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ScraperUtils {
    public static int interval(int value, int min, int max, String name) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(name + " should be between " + min + " and " + max + ": " + value);
        }
        return value;
    }

    public static int intervalMin(int value, int min, String name) {
        if (value < min) {
            throw new IllegalArgumentException(name + " should be greater than " + min + ": " + value);
        }
        return value;
    }

    public static int intervalMax(int value, int max, String name) {
        if (value > max) {
            throw new IllegalArgumentException(name + " should be lower than " + max + ": " + value);
        }
        return value;
    }
}
