package pkovacs.aoc.y2021;

import java.util.Comparator;
import java.util.PriorityQueue;

import pkovacs.aoc.AocUtils;
import pkovacs.util.InputUtils;
import pkovacs.util.alg.Dijkstra;
import pkovacs.util.alg.Dijkstra.Edge;
import pkovacs.util.data.IntTable;
import pkovacs.util.data.Tile;

public class Day15 {

    public static void main(String[] args) {
        var map = InputUtils.readCharMatrix(AocUtils.getInputPath());

//        System.out.println("Part 1: " + findDistance(map, 1));
//        System.out.println("Part 2: " + findDistance(map, 5));

        System.out.println("Part 1: " + findDistanceFast(map, 1));
        System.out.println("Part 2: " + findDistanceFast(map, 5));
    }

    private static long findDistance(char[][] map, int repeat) {
        int size = map.length;
        var table = new IntTable(size * repeat, size * repeat,
                (i, j) -> (InputUtils.parseInt(map[i % size][j % size]) + i / size + j / size - 1) % 9 + 1);

        var start = new Tile(0, 0);
        var target = new Tile(table.rowCount() - 1, table.colCount() - 1);
        var result = Dijkstra.findPath(start,
                tile -> table.neighborCells(tile).map(n -> new Edge<>(n, table.get(n))).toList(),
                target::equals);

        return result.orElseThrow().dist();
    }

    /**
     * Solves the puzzle using a simplified direct implementation of Dijkstra's algorithm (4-5 times faster).
     */
    private static long findDistanceFast(char[][] map, int repeat) {
        int size = map.length;
        var table = new IntTable(size * repeat, size * repeat,
                (i, j) -> (InputUtils.parseInt(map[i % size][j % size]) + i / size + j / size - 1) % 9 + 1);

        var start = new Tile(0, 0);
        var target = new Tile(table.rowCount() - 1, table.colCount() - 1);
        var dist = new IntTable(table.rowCount(), table.colCount());
        dist.fill(Integer.MAX_VALUE);
        dist.set(start, 0);

        var queue = new PriorityQueue<Tile>(Comparator.comparing(dist::get));
        queue.add(start);
        while (!queue.isEmpty()) {
            var u = queue.remove();
            int du = dist.get(u);
            table.neighborCells(u).forEach(v -> {
                int dv = du + table.get(v);
                if (dv < dist.get(v)) {
                    dist.set(v, dv);
                    queue.add(v);
                }
            });
        }

        return dist.get(target);
    }

}
