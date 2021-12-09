package pkovacs.aoc.y2021;

import java.util.HashMap;
import java.util.Map;

import com.google.common.primitives.Longs;
import pkovacs.aoc.AocUtils;
import pkovacs.util.InputUtils;
import pkovacs.util.alg.Bfs;
import pkovacs.util.data.Tile;

public class Day09 {

    public static void main(String[] args) {
        var input = InputUtils.readCharMatrix(AocUtils.getInputPath());
        var map = initMap(input);

        System.out.println("Part 1: " + solve1(map));
        System.out.println("Part 2: " + solve2(map));
    }

    private static long solve1(Map<Tile, Integer> map) {
        return map.keySet().stream()
                .filter(t -> isLowPoint(map, t))
                .mapToLong(t -> map.get(t) + 1)
                .sum();
    }

    private static long solve2(Map<Tile, Integer> map) {
        var basinSizes = map.keySet().stream()
                .filter(t -> isLowPoint(map, t))
                .mapToLong(t -> calculateBasinSize(map, t))
                .toArray();

        Longs.sortDescending(basinSizes);
        return basinSizes[0] * basinSizes[1] * basinSizes[2];
    }

    private static Map<Tile, Integer> initMap(char[][] input) {
        var map = new HashMap<Tile, Integer>();
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[i].length; j++) {
                map.put(new Tile(i, j), Integer.parseInt(String.valueOf(input[i][j])));
            }
        }
        return map;
    }

    private static boolean isLowPoint(Map<Tile, Integer> map, Tile tile) {
        return tile.neighbors().stream()
                .filter(map::containsKey)
                .allMatch(n -> map.get(n) > map.get(tile));
    }

    private static long calculateBasinSize(Map<Tile, Integer> map, Tile tile) {
        var bfsResult = Bfs.run(tile,
                t -> t.neighbors().stream()
                        .filter(n -> map.containsKey(n) && map.get(n) < 9)
                        .toList());
        return bfsResult.size();
    }

}
