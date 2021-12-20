package pkovacs.aoc.y2021;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pkovacs.aoc.AocUtils;
import pkovacs.util.InputUtils;
import pkovacs.util.data.CharTable;
import pkovacs.util.data.Tile;

public class Day20 {

    public static void main(String[] args) {
        var lines = InputUtils.readLines(AocUtils.getInputPath());

        var alg = lines.get(0);
        var imageInput = lines.subList(2, lines.size());

        System.out.println("Part 1: " + solve(alg, imageInput, 2));
        System.out.println("Part 2: " + solve(alg, imageInput, 50));
    }

    private static long solve(String alg, List<String> imageInput, int stepCount) {
        var img = new Image(imageInput);
        for (int step = 0; step < stepCount; step++) {
            // Build new image by shifting indices:
            // the pixel (i,j) of the new image is derived from the pixel (i-1,j-1) of the old image
            var newMap = new HashMap<Tile, Character>();
            for (int i = 0; i < img.size + 2; i++) {
                for (int j = 0; j < img.size + 2; j++) {
                    int k = 0;
                    for (int di = -2; di <= 0; di++) {
                        for (int dj = -2; dj <= 0; dj++) {
                            k = k * 2 + (img.isLit(i + di, j + dj) ? 1 : 0);
                        }
                    }
                    newMap.put(new Tile(i, j), alg.charAt(k));
                }
            }

            char others = alg.charAt(img.others == '.' ? 0 : alg.length() - 1);
            img = new Image(newMap, img.size + 2, others);
        }

        return img.map.entrySet().stream().filter(e -> e.getValue() == '#').count();
    }

    private static record Image(Map<Tile, Character> map, int size, char others) {

        Image(List<String> lines) {
            this(collectMap(lines), lines.size(), '.');
        }

        private static Map<Tile, Character> collectMap(List<String> lines) {
            var map = new HashMap<Tile, Character>();
            for (int i = 0; i < lines.size(); i++) {
                for (int j = 0; j < lines.get(i).length(); j++) {
                    map.put(new Tile(i, j), lines.get(i).charAt(j));
                }
            }
            return map;
        }

        boolean isLit(int r, int c) {
            return map.getOrDefault(new Tile(r, c), others) == '#';
        }

    }

}
