package pkovacs.aoc.y2021;

import java.util.stream.IntStream;

import pkovacs.aoc.AocUtils;
import pkovacs.util.InputUtils;

public class Day01 {

    public static void main(String[] args) {
        var depths = InputUtils.readInts(AocUtils.getInputPath());

        System.out.println("Part 1: " + countIncreases(depths, 1));
        System.out.println("Part 2: " + countIncreases(depths, 3));
    }

    // Note: (x + y + z < y + z + w) is equivalent to (x < w)
    private static long countIncreases(int[] values, int window) {
        return IntStream.range(0, values.length - window)
                .filter(i -> values[i] < values[i + window])
                .count();
    }

}
