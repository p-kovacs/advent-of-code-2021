package pkovacs.aoc.y2021;

import java.util.stream.LongStream;

import pkovacs.aoc.AocUtils;
import pkovacs.util.InputUtils;

public class Day06 {

    public static void main(String[] args) {
        var input = InputUtils.readInts(AocUtils.getInputPath());

        System.out.println("Part 1: " + solve(input, 80));
        System.out.println("Part 2: " + solve(input, 256));
    }

    private static long solve(int[] input, int days) {
        long[] counts = new long[9]; // counts[i] is the number of fishes with timer value i (for the current day)
        for (int i : input) {
            counts[i]++;
        }

        for (int day = 0; day < days; day++) {
            long newborn = counts[0];
            for (int i = 0; i < counts.length - 1; i++) {
                counts[i] = counts[i + 1];
            }
            counts[6] += newborn;
            counts[8] = newborn;
        }

        return LongStream.of(counts).sum();
    }

}
