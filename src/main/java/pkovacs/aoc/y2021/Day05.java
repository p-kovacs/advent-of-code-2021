package pkovacs.aoc.y2021;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import pkovacs.aoc.AocUtils;
import pkovacs.util.InputUtils;

public class Day05 {

    private static final int MAP_SIZE = 1000;

    public static void main(String[] args) {
        var input = InputUtils.readLines(AocUtils.getInputPath());

        System.out.println("Part 1: " + solve(input, false));
        System.out.println("Part 2: " + solve(input, true));
    }

    public static long solve(List<String> input, boolean includeDiagonals) {
        int[][] map = new int[MAP_SIZE][MAP_SIZE];
        for (var line : input) {
            var v = InputUtils.parseInts(line);
            int sx = v[0];
            int sy = v[1];
            int tx = v[2];
            int ty = v[3];

            if (sy == ty) {
                IntStream.rangeClosed(Math.min(sx, tx), Math.max(sx, tx)).forEach(x -> map[x][sy]++);
            } else if (sx == tx) {
                IntStream.rangeClosed(Math.min(sy, ty), Math.max(sy, ty)).forEach(y -> map[sx][y]++);
            } else if (includeDiagonals) {
                int dist = Math.abs(sx - tx);
                int dx = (tx - sx) / dist;
                int dy = (ty - sy) / dist;
                IntStream.rangeClosed(0, dist).forEach(i -> map[sx + dx * i][sy + dy * i]++);
            }
        }

        return IntStream.range(0, MAP_SIZE)
                .mapToLong(i -> Arrays.stream(map[i]).filter(v -> v > 1).count())
                .sum();
    }

}
