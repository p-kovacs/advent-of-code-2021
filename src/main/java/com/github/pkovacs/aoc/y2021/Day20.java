package com.github.pkovacs.aoc.y2021;

import java.util.List;

import com.github.pkovacs.util.InputUtils;
import com.github.pkovacs.util.data.CharTable;
import com.github.pkovacs.util.data.Tile;

public class Day20 {

    public static void main(String[] args) {
        var lines = InputUtils.readLines(AocUtils.getInputPath());

        var alg = lines.get(0);
        var imageInput = lines.subList(2, lines.size());

        System.out.println("Part 1: " + solve(alg, imageInput, 2));
        System.out.println("Part 2: " + solve(alg, imageInput, 50));
    }

    private static long solve(String alg, List<String> imageInput, int stepCount) {
        var img = new Image(new CharTable(imageInput), '.');
        for (int step = 0; step < stepCount; step++) {
            img = img.enhance(alg);
        }
        return img.table.count('#');
    }

    /**
     * Stores an image as a {@link CharTable} and a single character that represents the infinite "border"
     * around the table.
     */
    private static record Image(CharTable table, char border) {

        /**
         * Builds the "enhanced" image from this image according to the given "algorithm".
         * Indices are shifted to ensure that they remain non-negative: the pixel (i,j) of the returned image
         * is derived from the pixel (i-1,j-1) of this image.
         */
        private Image enhance(String alg) {
            var newTable = new CharTable(table.rowCount() + 2, table.colCount() + 2,
                    (i, j) -> alg.charAt(getAlgIndex(i - 1, j - 1)));
            char newBorder = alg.charAt(border == '.' ? 0 : alg.length() - 1);
            return new Image(newTable, newBorder);
        }

        private int getAlgIndex(int i, int j) {
            return Tile.stream(i - 1, j - 1, i + 2, j + 2)
                    .mapToInt(this::getBit)
                    .reduce(0, (a, b) -> a << 1 | b);
        }

        private int getBit(Tile tile) {
            return (table.containsCell(tile) ? table.get(tile) : border) == '#' ? 1 : 0;
        }

    }

}
