package com.github.pkovacs.aoc.y2021;

import java.util.List;

import com.github.pkovacs.util.InputUtils;

public class Day02 {

    public static void main(String[] args) {
        var lines = InputUtils.readLines(AocUtils.getInputPath());

        System.out.println("Part 1: " + solve(lines, false));
        System.out.println("Part 2: " + solve(lines, true));
    }

    private static long solve(List<String> lines, boolean advanced) {
        long x = 0;
        long depth = 0;
        long aim = 0;
        for (var line : lines) {
            var parts = line.split(" ");
            int v = Integer.parseInt(parts[1]);
            switch (parts[0]) {
                case "forward" -> {
                    x += v;
                    depth += aim * v;
                }
                case "up" -> aim -= v;
                case "down" -> aim += v;
            }
        }
        return advanced ? x * depth : x * aim;
    }

}
