package com.github.pkovacs.aoc.y2021;

import java.util.List;
import java.util.stream.Stream;

import com.github.pkovacs.util.InputUtils;

public class Day03 {

    public static void main(String[] args) {
        var input = InputUtils.readLines(AocUtils.getInputPath());

        long part1 = calculateRate(input, true) * calculateRate(input, false);
        long part2 = calculateRating(input, true) * calculateRating(input, false);

        System.out.println("Part 1: " + part1);
        System.out.println("Part 2: " + part2);
    }

    private static Stream<String> filter(List<String> list, int index, char ch) {
        return list.stream().filter(s -> s.charAt(index) == ch);
    }

    private static int getPreferredBit(List<String> list, int index, boolean mostCommon) {
        long count = filter(list, index, '1').count();
        int mostCommonBit = count >= list.size() - count ? 1 : 0;
        return mostCommon ? mostCommonBit : 1 - mostCommonBit;
    }

    private static long calculateRate(List<String> list, boolean mostCommon) {
        int bitCount = list.get(0).length();
        var sb = new StringBuilder();
        for (int i = 0; i < bitCount; i++) {
            sb.append(getPreferredBit(list, i, mostCommon));
        }
        return Long.parseLong(sb.toString(), 2);
    }

    private static long calculateRating(List<String> input, boolean mostCommon) {
        int bitCount = input.get(0).length();
        List<String> list = input;
        for (int i = 0; i < bitCount && list.size() > 1; i++) {
            char bit = getPreferredBit(list, i, mostCommon) == 1 ? '1' : '0';
            list = filter(list, i, bit).toList();
        }
        return Long.parseLong(list.get(0), 2);
    }

}
