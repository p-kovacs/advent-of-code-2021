package pkovacs.aoc.y2021;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import pkovacs.aoc.AocUtils;
import pkovacs.util.InputUtils;

public class Day04 {

    public static void main(String[] args) {
        var input = InputUtils.readString(AocUtils.getInputPath());
        var parts= input.split("\n\n", 2);

        var selected = InputUtils.parseInts(parts[0]);
        var boardInput = InputUtils.collectLineBlocks(parts[1]);

        var boards = new ArrayList<Board>();
        boardInput.stream().map(Board::new).forEach(boards::add);

        var winnerScores = new ArrayList<Long>();
        for (int num : selected) {
            for (var board : boards) {
                board.mark(num);
                if (board.isWinner()) {
                    winnerScores.add(num * board.getSumOfNonMarked());
                }
            }
            boards.removeIf(Board::isWinner);
        }

        System.out.println("Part 1: " + winnerScores.get(0));
        System.out.println("Part 2: " + winnerScores.get(winnerScores.size() - 1));
    }

    private static class Board {

        static final int SIZE = 5;

        int[][] data;

        public Board(List<String> input) {
            data = new int[SIZE][];
            for (int i = 0; i < SIZE; i++) {
                data[i] = InputUtils.parseInts(input.get(i));
            }
        }

        void mark(int num) {
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (data[i][j] == num) {
                        data[i][j] = -1;
                    }
                }
            }
        }

        boolean isWinner() {
            return IntStream.range(0, SIZE).anyMatch(k -> isMarkedRow(k) || isMarkedCol(k));
        }

        boolean isMarkedRow(int index) {
            return IntStream.range(0, SIZE).allMatch(k -> data[index][k] == -1);
        }

        boolean isMarkedCol(int index) {
            return IntStream.range(0, SIZE).allMatch(k -> data[k][index] == -1);
        }

        long getSumOfNonMarked() {
            long sum = 0;
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (data[i][j] > -1) {
                        sum += data[i][j];
                    }
                }
            }
            return sum;
        }

    }

}
