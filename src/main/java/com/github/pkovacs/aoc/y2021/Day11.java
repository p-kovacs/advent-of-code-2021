package com.github.pkovacs.aoc.y2021;

import java.util.ArrayDeque;
import java.util.List;

import com.github.pkovacs.util.InputUtils;
import com.github.pkovacs.util.data.IntTable;
import com.github.pkovacs.util.data.Tile;

public class Day11 {

    public static void main(String[] args) {
        var input = InputUtils.readCharMatrix(AocUtils.getInputPath());

        System.out.println("Part 1: " + solve(input, false));
        System.out.println("Part 2: " + solve(input, true));
    }

    private static long solve(char[][] input, boolean advanced) {
        var table = new IntTable(input.length, input[0].length, (i, j) -> InputUtils.parseInt(input[i][j]));

        long totalFlashCount = 0;
        int synchStep = 0;
        for (int s = 0, max = advanced ? Integer.MAX_VALUE : 100; s < max; s++) {
            table.updateAll(v -> v + 1);

            var queue = new ArrayDeque<>(collectFlashed(table));
            while (!queue.isEmpty()) {
                var tile = queue.remove();
                table.extendedNeighborCells(tile).forEach(c -> {
                    if (table.inc(c) == 10) {
                        queue.add(c);
                    }
                });
            }

            var flashed = collectFlashed(table);
            flashed.forEach(c -> table.set(c, 0));

            totalFlashCount += flashed.size();
            if (advanced && flashed.size() == table.size()) {
                synchStep = s + 1;
                break;
            }
        }

        return advanced ? synchStep : totalFlashCount;
    }

    private static List<Tile> collectFlashed(IntTable table) {
        return table.cells().filter(c -> table.get(c) > 9).toList();
    }

}
