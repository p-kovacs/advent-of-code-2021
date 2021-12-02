package pkovacs.aoc.y2021;

import java.util.List;

import pkovacs.aoc.AocUtils;
import pkovacs.util.InputUtils;

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
            int v = Integer.parseInt(line.split(" ")[1]);
            if (line.startsWith("forward")) {
                x += v;
                depth += aim * v;
            } else if (line.startsWith("up")) {
                aim -= v;
            } else if (line.startsWith("down")) {
                aim += v;
            }
        }
        return advanced ? x * depth : x * aim;
    }

}
