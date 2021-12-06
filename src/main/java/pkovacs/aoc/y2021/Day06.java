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
        // counts[i] is the number of fishes with timer value i
        long[] counts = new long[9];
        for (int i : input) {
            counts[i]++;
        }

        for (int day = 0; day < days; day++) {
            long[] newCounts = new long[9];
            for (int i = 0; i < 9; i++) {
                if (i == 0) {
                    newCounts[6] += counts[i];
                    newCounts[8] += counts[i];
                } else {
                    newCounts[i - 1] += counts[i];
                }
            }
            counts = newCounts;
        }

        return LongStream.of(counts).sum();
    }

}
