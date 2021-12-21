package pkovacs.aoc.y2021;

import java.util.function.Predicate;

import pkovacs.aoc.AocUtils;
import pkovacs.util.InputUtils;
import pkovacs.util.data.CounterMap;

public class Day21 {

    private static final int PART1_WINNER_SCORE = 1000;
    private static final int PART2_WINNER_SCORE = 21;
    private static final int PART2_DICE_SIDE_COUNT = 3;

    public static void main(String[] args) {
        var ints = InputUtils.readInts(AocUtils.getInputPath());

        int pos1 = ints[1];
        int pos2 = ints[3];

        System.out.println("Part 1: " + solve1(pos1, pos2));
        System.out.println("Part 2: " + solve2(pos1, pos2));
    }

    private static int solve1(int pos1, int pos2) {
        // Simulate the deterministic game
        var state = new State(pos1, 0, pos2, 0, true);
        int rollCount = 0;
        for (int d = 1; !state.hasWinner(PART1_WINNER_SCORE); d += 3) {
            int move = d + (d + 1) + (d + 2);
            state = state.next(move);
            rollCount += 3;
        }

        return rollCount * Math.min(state.score1, state.score2);
    }

    private static long solve2(int pos1, int pos2) {
        // Build the map of triple rolls (sum->count)
        var tripleRolls = new CounterMap<Integer>();
        for (int r1 = 1; r1 <= PART2_DICE_SIDE_COUNT; r1++) {
            for (int r2 = 1; r2 <= PART2_DICE_SIDE_COUNT; r2++) {
                for (int r3 = 1; r3 <= PART2_DICE_SIDE_COUNT; r3++) {
                    tripleRolls.inc(r1 + r2 + r3);
                }
            }
        }

        // Simulate the non-deterministic game: calculate possible states and their occurrence counts
        var map = new CounterMap<State>();
        map.put(new State(pos1, 0, pos2, 0, true), 1);

        while (map.keySet().stream().anyMatch(s -> !s.hasWinner(PART2_WINNER_SCORE))) {
            var newMap = new CounterMap<State>();
            map.forEach((state, stateCount) -> {
                if (state.hasWinner(PART2_WINNER_SCORE)) {
                    // The state already has a winner: preserve it with the current count
                    newMap.add(state, stateCount);
                } else {
                    // The state does not have a winner: add possible next steps with appropriate counts
                    tripleRolls.forEach((move, moveCount) -> {
                        newMap.add(state.next(move), stateCount * moveCount);
                    });
                }
            });
            map = newMap;
        }

        long winCount1 = sum(map, s -> s.score1 >= PART2_WINNER_SCORE);
        long winCount2 = sum(map, s -> s.score2 >= PART2_WINNER_SCORE);
        return Math.max(winCount1, winCount2);
    }

    private static long sum(CounterMap<State> map, Predicate<State> predicate) {
        return map.keySet().stream().filter(predicate).mapToLong(map::get).sum();
    }

    private static record State(int pos1, int score1, int pos2, int score2, boolean next1) {

        State next(int move) {
            int newPos = ((next1 ? pos1 : pos2) + move - 1) % 10 + 1;
            return next1
                    ? new State(newPos, score1 + newPos, pos2, score2, false)
                    : new State(pos1, score1, newPos, score2 + newPos, true);
        }

        boolean hasWinner(int winnerScore) {
            return score1 >= winnerScore || score2 >= winnerScore;
        }

    }

}
