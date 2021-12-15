package pkovacs.aoc.y2021;

import pkovacs.aoc.AocUtils;
import pkovacs.util.InputUtils;
import pkovacs.util.alg.Dijkstra;
import pkovacs.util.alg.Dijkstra.Edge;
import pkovacs.util.data.IntTable;
import pkovacs.util.data.Tile;

public class Day15 {

    public static void main(String[] args) {
        var map = InputUtils.readCharMatrix(AocUtils.getInputPath());

        System.out.println("Part 1: " + findDistance(map, 1));
        System.out.println("Part 2: " + findDistance(map, 5));
    }

    private static long findDistance(char[][] map, int repeat) {
        int size = map.length;
        var table = new IntTable(size * repeat, size * repeat,
                (i, j) -> (InputUtils.parseInt(map[i % size][j % size]) + i / size + j / size - 1) % 9 + 1);

        var start = new Tile(0, 0);
        var target = new Tile(table.rowCount() - 1, table.colCount() - 1);
        var path = Dijkstra.findPath(start,
                tile -> table.neighborCells(tile).map(n -> new Edge<>(n, table.get(n))).toList(),
                target::equals);

        return path.orElseThrow().dist();
    }

}
