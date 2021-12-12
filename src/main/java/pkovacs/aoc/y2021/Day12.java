package pkovacs.aoc.y2021;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import pkovacs.aoc.AocUtils;
import pkovacs.util.InputUtils;

import static java.util.stream.Collectors.toSet;

public class Day12 {

    public static void main(String[] args) {
        var input = InputUtils.readLines(AocUtils.getInputPath());

        System.out.println("Part 1: " + solve(input, false));
        System.out.println("Part 2: " + solve(input, true));
    }

    private static long solve(List<String> input, boolean advanced) {
        var graph = buildGraph(input);
        var smallCaves = graph.keySet().stream().filter(s -> s.toLowerCase().equals(s)).collect(toSet());

        long pathCount = 0;
        var queue = new ArrayDeque<List<String>>();
        queue.add(List.of("start"));
        while (!queue.isEmpty()) {
            var path = queue.remove();
            if ("end".equals(lastElement(path))) {
                pathCount++;
            } else {
                var newPaths = graph.get(lastElement(path)).stream()
                        .map(next -> append(path, next))
                        .filter(p -> isAcceptedPath(p, smallCaves, advanced))
                        .toList();
                queue.addAll(newPaths);
            }
        }
        return pathCount;
    }

    private static boolean isAcceptedPath(List<String> path, Collection<String> smallCaves, boolean advanced) {
        int maxTwice = advanced ? 1 : 0;
        return smallCaves.stream().allMatch(s -> count(path, s) <= 2)
                && smallCaves.stream().filter(s -> count(path, s) == 2).count() <= maxTwice;
    }

    private static Multimap<String, String> buildGraph(List<String> input) {
        Multimap<String, String> graph = MultimapBuilder.hashKeys().hashSetValues().build();
        for (var line : input) {
            var caves = line.split("-");
            graph.put(caves[0], caves[1]);
            graph.put(caves[1], caves[0]);
        }
        // Simplify directed graph: do not allow going back to "start"
        graph.entries().removeIf(e -> "start".equals(e.getValue()));
        return graph;
    }

    // Why don't have a method for this in Java?
    private static <T> T lastElement(List<T> list) {
        return list.get(list.size() - 1);
    }

    // Why don't have a method for this in Java?
    private static <T> int count(List<T> list, T value) {
        return (int) list.stream().filter(value::equals).count();
    }

    private static <T> List<T> append(List<T> list, T value) {
        return Stream.concat(list.stream(), Stream.of(value)).toList();
    }

}
