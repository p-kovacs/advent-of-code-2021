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

    // An alternative solution using dynamic programming
    private static long solve2(int[] input, int days) {
        long[] newFishes = new long[days + 1]; // newFishes[i] is the number of newborn fishes after i days
        for (int i : input) {
            newFishes[i + 1]++;
        }
        newFishes[8] += newFishes[1];
        newFishes[9] += newFishes[2];

        for (int day = 10; day <= days; day++) {
            newFishes[day] = newFishes[day - 7] + newFishes[day - 9];
        }

        return input.length + LongStream.of(newFishes).sum();
    }

}
