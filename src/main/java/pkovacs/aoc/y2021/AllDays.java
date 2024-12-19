package pkovacs.aoc.y2021;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Consumer;

/**
 * Verifies the solution for each day against the expected answers for my puzzle input files.
 */
public class AllDays {

    private static final List<Day> DAYS = List.of(
            new Day("Day 01", Day01::main, "1301", "1346"),
            new Day("Day 02", Day02::main, "1693300", "1857958050"),
            new Day("Day 03", Day03::main, "2003336", "1877139"),
            new Day("Day 04", Day04::main, "16674", "7075"),
            new Day("Day 05", Day05::main, "5167", "17604"),
            new Day("Day 06", Day06::main, "387413", "1738377086345"),
            new Day("Day 07", Day07::main, "325528", "85015836"),
            new Day("Day 08", Day08::main, "362", "1020159"),
            new Day("Day 09", Day09::main, "526", "1123524"),
            new Day("Day 10", Day10::main, "167379", "2776842859"),
            new Day("Day 11", Day11::main, "1599", "418"),
            new Day("Day 12", Day12::main, "3292", "89592"),
            new Day("Day 13", Day13::main, "790", "96"),
            new Day("Day 14", Day14::main, "2112", "3243771149914"),
            new Day("Day 15", Day15::main, "393", "2823"),
            new Day("Day 16", Day16::main, "981", "299227024091"),
            new Day("Day 17", Day17::main, "10878", "4716"),
            new Day("Day 18", Day18::main, "3935", "4669"),
            new Day("Day 19", Day19::main, "303", "9621"),
            new Day("Day 20", Day20::main, "5489", "19066"),
            new Day("Day 21", Day21::main, "752247", "221109915584112"),
            new Day("Day 22", Day22::main, "607573", "1267133912086024"),
            new Day("Day 23", Day23::main, "18170", "50208"),
            new Day("Day 24", Day24::main, "92915979999498", "21611513911181"),
            new Day("Day 25", Day25::main, "321", "0")
    );

    public static void main(String[] args) {
        String format = "%-12s%-8s%-8s%8s%n";
        System.out.printf(format, "Day", "Part 1", "Part 2", "Time");

        DAYS.stream().filter(day -> day.mainMethod != null).forEach(day -> {
            long start = System.nanoTime();
            var results = runDay(day);
            long time = (System.nanoTime() - start) / 1_000_000L;

            System.out.printf(format, day.name, evaluate(day, results, 0), evaluate(day, results, 1), time + " ms");
        });
    }

    private static String evaluate(Day day, List<String> results, int index) {
        var expected = index == 0 ? day.expected1 : day.expected2;
        return results.size() == 2 && expected.equals(results.get(index)) ? "\u2714" : "FAILED";
    }

    private static List<String> runDay(Day day) {
        var origOut = System.out;
        try {
            var out = new ByteArrayOutputStream(200);
            System.setOut(new PrintStream(out));
            day.mainMethod.accept(null);
            return out.toString(StandardCharsets.UTF_8).lines().map(l -> l.split(": ")[1]).toList();
        } catch (Exception e) {
            return List.of();
        } finally {
            System.setOut(origOut);
        }
    }

    private record Day(String name, Consumer<String[]> mainMethod, String expected1, String expected2) {}

}
