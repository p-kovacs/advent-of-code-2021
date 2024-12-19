package com.github.pkovacs.aoc.y2021;

import java.util.function.LongUnaryOperator;
import java.util.stream.IntStream;

import com.github.pkovacs.util.InputUtils;

public class Day07 {

    public static void main(String[] args) {
        var input = InputUtils.readInts(AocUtils.getInputPath());

        System.out.println("Part 1: " + solve(input, d -> d));
        System.out.println("Part 2: " + solve(input, d -> d * (d + 1) / 2));
    }

    private static long solve(int[] input, LongUnaryOperator costFunction) {
        int min = IntStream.of(input).min().orElseThrow();
        int max = IntStream.of(input).max().orElseThrow();

        return IntStream.rangeClosed(min, max)
                .mapToLong(i -> IntStream.of(input).mapToLong(j -> Math.abs(i - j)).map(costFunction).sum())
                .min().orElseThrow();
    }

}
