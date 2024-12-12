package pkovacs.aoc.y2021;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import pkovacs.util.InputUtils;

import static java.util.stream.Collectors.toSet;

public class Day12 {

    public static void main(String[] args) {
        var input = InputUtils.readLines(AocUtils.getInputPath());

//        System.out.println("Part 1: " + solveSimple(input, false));
//        System.out.println("Part 2: " + solveSimple(input, true));

        System.out.println("Part 1: " + solve(input, false));
        System.out.println("Part 2: " + solve(input, true));
    }

    /**
     * Simple, straightforward solution for the problem.
     */
    private static long solveSimple(List<String> input, boolean advanced) {
        var graph = buildGraph(input);
        var smallCaves = graph.keySet().stream().filter(Day12::isSmallCave).collect(toSet());

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

    /**
     * Represents a state of cave traversal: where we are and how we got there.
     * Only those properties of the path is stored that determine how we can continue from here. For example,
     * the order of previously visited caves are not relevant.
     */
    private record State(String end, Set<String> smallCaves, boolean allowRepeat) {}

    /**
     * Optimized solution for the problem that does not find each individual path, only calculates the path count.
     * Paths that can be continued in exactly the same way are grouped and handled together.
     */
    private static long solve(List<String> input, boolean advanced) {
        var graph = buildGraph(input);

        // BFS search of reachable states
        long pathCount = 0;
        var start = new State("start", Set.of(), advanced);
        var queue = new ArrayDeque<State>();
        var processing = new HashSet<State>();
        var pathCounts = new HashMap<State, Long>();
        queue.add(start);
        processing.add(start);
        pathCounts.put(start, 1L);
        while (!queue.isEmpty()) {
            var state = queue.pop();
            processing.remove(state);
            if ("end".equals(state.end())) {
                pathCount += pathCounts.get(state);
            } else {
                // Collect new states: the next cave is feasible if it is not small or not visited yet or the
                // current state (still) allows a single small cave to be visited twice
                var newStates = graph.get(state.end()).stream()
                        .filter(c -> !isSmallCave(c) || !state.smallCaves().contains(c) || state.allowRepeat)
                        .map(c -> new State(c, addSmallCave(state.smallCaves, c),
                                state.allowRepeat && !state.smallCaves().contains(c)))
                        .toList();
                for (var newState : newStates) {
                    if (processing.contains(newState)) {
                        pathCounts.computeIfPresent(newState, (k, v) -> v + pathCounts.get(state));
                    } else {
                        processing.add(newState);
                        queue.add(newState);
                        pathCounts.put(newState, pathCounts.get(state));
                    }
                }
            }
        }
        return pathCount;
    }

    private static boolean isSmallCave(String cave) {
        char ch = cave.charAt(0);
        return ch >= 'a' && ch <= 'z' && !"start".equals(cave);
    }

    private static Set<String> addSmallCave(Set<String> set, String cave) {
        return !isSmallCave(cave) || set.contains(cave)
                ? set
                : Stream.concat(set.stream(), Stream.of(cave)).collect(toSet());
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
