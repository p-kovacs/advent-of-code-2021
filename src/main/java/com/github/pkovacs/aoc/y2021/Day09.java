package com.github.pkovacs.aoc.y2021;

import java.util.ArrayList;

import com.google.common.primitives.Longs;
import com.github.pkovacs.util.InputUtils;
import com.github.pkovacs.util.alg.Bfs;
import com.github.pkovacs.util.data.IntTable;
import com.github.pkovacs.util.data.Tile;

public class Day09 {

    public static void main(String[] args) {
        var input = InputUtils.readCharMatrix(AocUtils.getInputPath());
        var table = new IntTable(input.length, input[0].length, (i, j) -> InputUtils.parseInt(input[i][j]));

        System.out.println("Part 1: " + solve1(table));
        System.out.println("Part 2: " + solve2(table));
//        System.out.println("Part 2: " + solve2Recursive(input)); // alternative solution
    }

    private static long solve1(IntTable table) {
        return table.cells()
                .filter(t -> isLowPoint(table, t))
                .mapToLong(t -> table.get(t) + 1)
                .sum();
    }

    private static long solve2(IntTable table) {
        var basinSizes = table.cells()
                .filter(t -> isLowPoint(table, t))
                .mapToLong(t -> calculateBasinSize(table, t))
                .toArray();

        Longs.sortDescending(basinSizes);
        return basinSizes[0] * basinSizes[1] * basinSizes[2];
    }

    private static boolean isLowPoint(IntTable table, Tile tile) {
        return table.neighborCells(tile).allMatch(n -> table.get(n) > table.get(tile));
    }

    private static long calculateBasinSize(IntTable table, Tile tile) {
        var bfsResult = Bfs.run(tile,
                t -> table.neighborCells(t).filter(n -> table.get(n) != 9).toList());
        return bfsResult.size();
    }

    private static long solve2Recursive(char[][] map) {
        var basinSizes = new ArrayList<Long>();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] != '9') {
                    basinSizes.add(calculateBasinSizeRecursive(map, i, j));
                }
            }
        }

        var array = Longs.toArray(basinSizes);
        Longs.sortDescending(array);
        return array[0] * array[1] * array[2];
    }

    private static long calculateBasinSizeRecursive(char[][] map, int i, int j) {
        if (i < 0 || i >= map.length || j < 0 || j >= map[0].length || map[i][j] == '9') {
            return 0;
        } else {
            map[i][j] = '9';
            return 1
                    + calculateBasinSizeRecursive(map, i - 1, j)
                    + calculateBasinSizeRecursive(map, i + 1, j)
                    + calculateBasinSizeRecursive(map, i, j - 1)
                    + calculateBasinSizeRecursive(map, i, j + 1);
        }
    }

}
