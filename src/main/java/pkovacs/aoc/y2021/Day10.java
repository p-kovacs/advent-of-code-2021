package pkovacs.aoc.y2021;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;

import pkovacs.util.InputUtils;

public class Day10 {

    public static void main(String[] args) {
        var lines = InputUtils.readLines(AocUtils.getInputPath());

        long invalidScore = 0;
        var validScores = new ArrayList<Long>();
        for (var line : lines) {
            var stack = new ArrayDeque<Character>();
            boolean valid = true;
            for (int i = 0; i < line.length() && valid; i++) {
                char ch = line.charAt(i);
                switch (ch) {
                    case '(', '[', '{', '<' -> stack.push(opposite(ch));
                    case ')', ']', '}', '>' -> {
                        if (stack.isEmpty() || !stack.pop().equals(ch)) {
                            invalidScore += invalidScore(ch);
                            valid = false;
                        }
                    }
                }
            }

            if (valid) {
                long score = 0;
                while (!stack.isEmpty()) {
                    score = score * 5 + validScore(stack.pop());
                }
                validScores.add(score);
            }
        }
        Collections.sort(validScores);

        System.out.println("Part 1: " + invalidScore);
        System.out.println("Part 2: " + validScores.get(validScores.size() / 2));
    }

    private static Character opposite(char ch) {
        return switch (ch) {
            case '(' -> ')';
            case '[' -> ']';
            case '{' -> '}';
            case '<' -> '>';
            default -> throw new IllegalArgumentException();
        };
    }

    private static int validScore(char ch) {
        return switch (ch) {
            case ')' -> 1;
            case ']' -> 2;
            case '}' -> 3;
            case '>' -> 4;
            default -> throw new IllegalArgumentException();
        };
    }

    private static int invalidScore(char ch) {
        return switch (ch) {
            case ')' -> 3;
            case ']' -> 57;
            case '}' -> 1197;
            case '>' -> 25137;
            default -> throw new IllegalArgumentException();
        };
    }

}
