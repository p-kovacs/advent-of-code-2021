package com.github.pkovacs.aoc.y2021;

import java.lang.StackWalker.Option;
import java.nio.file.Path;
import java.util.Locale;

/**
 * Provides a helper method to locate puzzle input files.
 */
public final class AocUtils {

    private AocUtils() {
    }

    /**
     * Returns a {@link Path} object that locates the input file corresponding to the caller class.
     * For example, if this method is called from class {@code Day05}, then {@code "input/day05.txt"} is located.
     */
    public static Path getInputPath() {
        var cl = StackWalker.getInstance(Option.RETAIN_CLASS_REFERENCE).getCallerClass();
        var fileName = cl.getSimpleName().toLowerCase(Locale.ROOT) + ".txt";
        return Path.of("input", fileName);
    }

}
