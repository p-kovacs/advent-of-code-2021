package pkovacs.aoc.y2021;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import pkovacs.util.InputUtils;
import pkovacs.util.data.IntTable;

public class Day04 {

    public static void main(String[] args) {
        var input = InputUtils.readString(AocUtils.getInputPath());
        var blocks = InputUtils.collectLineBlocks(input);

        var selected = InputUtils.parseInts(blocks.get(0).get(0));
        var boardInput = blocks.subList(1, blocks.size());

        var boards = new ArrayList<Board>(boardInput.stream().map(Board::new).toList());

        var winnerScores = new ArrayList<Long>();
        for (int num : selected) {
            for (var board : boards) {
                board.mark(num);
                if (board.isWinner()) {
                    winnerScores.add(num * (long) board.values().filter(v -> v != -1).sum());
                }
            }
            boards.removeIf(Board::isWinner);
        }

        System.out.println("Part 1: " + winnerScores.get(0));
        System.out.println("Part 2: " + winnerScores.get(winnerScores.size() - 1));
    }

    private static class Board extends IntTable {

        Board(List<String> lines) {
            super(lines.stream().map(InputUtils::parseInts).toArray(int[][]::new));
        }

        void mark(int num) {
            updateAll(v -> v == num ? -1 : v);
        }

        boolean isWinner() {
            return IntStream.range(0, rowCount())
                    .anyMatch(i -> rowValues(i).allMatch(v -> v == -1) || colValues(i).allMatch(v -> v == -1));
        }

    }

}
