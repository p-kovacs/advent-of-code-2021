package com.github.pkovacs.aoc.y2021;

import java.util.List;

import com.github.pkovacs.util.InputUtils;
import com.github.pkovacs.util.data.CharTable;
import com.github.pkovacs.util.data.Tile;

public class Day25 {

    public static void main(String[] args) {
        var input = InputUtils.readCharMatrix(AocUtils.getInputPath());

        System.out.println("Part 1: " + solve(input));
        System.out.println("Part 2: " + 0);
    }

    private static int solve(char[][] input) {
        var table = new CharTable(input);
        int stepCount = 0;
        while (true) {
            stepCount++;
            boolean changed = false;

            var snapshot = new CharTable(table);
            for (var from : cells(table, '>')) {
                var to = new Tile(from.row(), (from.col() + 1) % table.colCount());
                if (isEmpty(snapshot, to)) {
                    changed = true;
                    move(table, from, to);
                }
            }
            snapshot = new CharTable(table);
            for (var from : cells(table, 'v')) {
                var to = new Tile((from.row() + 1) % table.rowCount(), from.col());
                if (isEmpty(snapshot, to)) {
                    changed = true;
                    move(table, from, to);
                }
            }

            if (!changed) {
                break;
            }
        }

        return stepCount;
    }

    private static List<Tile> cells(CharTable table, char c) {
        return table.cells().filter(t -> table.get(t) == c).toList();
    }

    private static boolean isEmpty(CharTable table, Tile t) {
        return table.get(t) == '.';
    }

    private static void move(CharTable table, Tile from, Tile to) {
        table.set(to, table.get(from));
        table.set(from, '.');
    }

}
