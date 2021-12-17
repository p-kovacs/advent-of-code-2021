package pkovacs.aoc.y2021;

import java.util.ArrayList;

import com.google.common.primitives.Longs;
import pkovacs.aoc.AocUtils;
import pkovacs.util.InputUtils;
import pkovacs.util.alg.Bfs;
import pkovacs.util.data.IntTable;
import pkovacs.util.data.Tile;

public class Day09 {

    public static void main(String[] args) {
        var input = InputUtils.readCharMatrix(AocUtils.getInputPath());
        var table = new IntTable(input.length, input[0].length, (i, j) -> InputUtils.parseInt(input[i][j]));

        System.out.println("Part 1: " + solve1(table));
        System.out.println("Part 2: " + solve2(table));
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

}
