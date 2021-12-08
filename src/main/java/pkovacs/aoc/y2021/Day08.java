package pkovacs.aoc.y2021;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;
import pkovacs.aoc.AocUtils;
import pkovacs.util.InputUtils;

public class Day08 {

    public static void main(String[] args) {
        var lines = InputUtils.readLines(AocUtils.getInputPath());

        System.out.println("Part 1: " + solve1(lines));
        System.out.println("Part 2: " + solve2(lines));
    }

    private static long solve1(List<String> lines) {
        return lines.stream().mapToLong(line ->
                Arrays.stream(line.split(" \\| ")[1].split(" "))
                        .mapToInt(String::length).filter(i -> i == 2 || i == 3 || i == 4 || i == 7)
                        .count()).sum();
    }

    private static int solve2(List<String> lines) {
        int sum = 0;
        for (var line : lines) {
            var parts = line.split(" \\| ");
            var digits = List.of(parts[0].split(" "));
            var output = List.of(parts[1].split(" "));

            var codes = new HashMap<Integer, Set<Character>>();

            addCode(codes, digits, 1, c -> c.size() == 2);
            addCode(codes, digits, 4, c -> c.size() == 4);
            addCode(codes, digits, 7, c -> c.size() == 3);
            addCode(codes, digits, 8, c -> c.size() == 7);

            addCode(codes, digits, 3, c -> c.size() == 5 && c.containsAll(codes.get(1)));
            addCode(codes, digits, 5, c -> c.size() == 5
                    && c.containsAll(Sets.difference(codes.get(4), codes.get(1))));
            addCode(codes, digits, 2, c -> c.size() == 5);

            addCode(codes, digits, 9, c -> c.size() == 6 && c.containsAll(codes.get(4)));
            addCode(codes, digits, 0, c -> c.size() == 6 && c.containsAll(codes.get(1)));
            addCode(codes, digits, 6, c -> c.size() == 6);

            var sb = new StringBuilder();
            for (var s : output) {
                var key = codes.entrySet().stream()
                        .filter(e -> e.getValue().equals(code(s)))
                        .map(Entry::getKey)
                        .findFirst().orElseThrow();
                sb.append(key);
            }
            sum += Integer.parseInt(sb.toString());
        }
        return sum;
    }

    private static Set<Character> code(String s) {
        return InputUtils.stream(s).collect(Collectors.toSet());
    }

    private static void addCode(Map<Integer, Set<Character>> codes, List<String> digits,
            int key, Predicate<Set<Character>> predicate) {
        var digit = digits.stream()
                .map(Day08::code)
                .filter(c -> !codes.containsValue(c))
                .filter(predicate)
                .findFirst()
                .orElseThrow();
        codes.put(key, digit);
    }

}
