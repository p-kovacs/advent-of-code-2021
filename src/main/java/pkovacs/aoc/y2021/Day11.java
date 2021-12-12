package pkovacs.aoc.y2021;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pkovacs.aoc.AocUtils;
import pkovacs.util.InputUtils;
import pkovacs.util.data.Tile;

public class Day11 {

    public static void main(String[] args) {
        var input = InputUtils.readCharMatrix(AocUtils.getInputPath());

        System.out.println("Part 1: " + solve(input, false));
        System.out.println("Part 2: " + solve(input, true));
    }

    private static long solve(char[][] input, boolean advanced) {
        var map = buildMap(input);

        long totalFlashCount = 0;
        int synchStep = 0;
        for (int s = 0, max = advanced ? Integer.MAX_VALUE : 100; s < max; s++) {
            map.forEach((c, v) -> map.put(c, v + 1));

            var queue = new ArrayDeque<>(collectFlashed(map));
            while (!queue.isEmpty()) {
                var tile = queue.remove();
                tile.extendedNeighbors().stream()
                        .filter(t -> t.isValid(input.length, input[0].length))
                        .forEach(c -> {
                            if (map.compute(c, (k, v) -> v + 1) == 10) {
                                queue.add(c);
                            }
                        });
            }

            var flashed = collectFlashed(map);
            flashed.forEach(c -> map.put(c, 0));

            totalFlashCount += flashed.size();
            if (advanced && flashed.size() == map.size()) {
                synchStep = s + 1;
                break;
            }
        }

        return advanced ? synchStep : totalFlashCount;
    }

    private static List<Tile> collectFlashed(Map<Tile, Integer> map) {
        return map.keySet().stream().filter(c -> map.get(c) > 9).toList();
    }

    private static Map<Tile, Integer> buildMap(char[][] input) {
        var map = new HashMap<Tile, Integer>();
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[0].length; j++) {
                map.put(new Tile(i, j), Integer.parseInt(String.valueOf(input[i][j])));
            }
        }
        return map;
    }

}
