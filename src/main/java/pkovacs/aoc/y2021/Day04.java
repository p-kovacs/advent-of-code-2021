package pkovacs.aoc.y2021;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import pkovacs.aoc.AocUtils;
import pkovacs.util.InputUtils;

public class Day04 {

    public static void main(String[] args) {
        var input = InputUtils.readString(AocUtils.getInputPath());
        var parts = input.split("\n\n", 2);

        var selected = InputUtils.parseInts(parts[0]);
        var boardInput = parts[1].split("\n\n");

        var boards = new ArrayList<Board>();
        Stream.of(boardInput).map(Board::new).forEach(boards::add);

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

        int[] data;

        public Board(String input) {
            data = InputUtils.parseInts(input);
            if (data.length != SIZE * SIZE) {
                throw new IllegalArgumentException();
            }
        }

        void mark(int num) {
            IntStream.range(0, data.length).filter(i -> data[i] == num).forEach(i -> data[i] = -1);
        }

        boolean isWinner() {
            return IntStream.range(0, SIZE).anyMatch(k -> isMarkedRow(k) || isMarkedCol(k));
        }

        boolean isMarkedRow(int index) {
            return IntStream.range(0, SIZE).allMatch(k -> data[index * SIZE + k] == -1);
        }

        boolean isMarkedCol(int index) {
            return IntStream.range(0, SIZE).allMatch(k -> data[index + k * SIZE] == -1);
        }

        long getSumOfNonMarked() {
            return IntStream.of(data).filter(x -> x != -1).sum();
        }

    }

}
