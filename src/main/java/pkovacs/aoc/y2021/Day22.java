package pkovacs.aoc.y2021;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.UnaryOperator;

import pkovacs.aoc.AocUtils;
import pkovacs.util.InputUtils;

public class Day22 {

    public static void main(String[] args) {
        var input = InputUtils.readLines(AocUtils.getInputPath());

        var initArea = new Cuboid(-50, 50, -50, 50, -50, 50);

        System.out.println("Part 1: " + solve(input, initArea));
        System.out.println("Part 2: " + solve(input, null));
    }

    private static long solve(List<String> input, Cuboid area) {
        var set = new HashSet<Cuboid>(); // contains a set of disjoint cuboids that are "on"
        for (var line : input) {
            var parts = line.split(" ");
            var cuboid = new Cuboid(InputUtils.parseInts(parts[1]));

            var newSet = new HashSet<Cuboid>();
            set.forEach(c -> newSet.addAll(c.remove(cuboid)));
            if ("on".equals(parts[0])) {
                newSet.add(cuboid);
            }

            set = newSet;
        }

        UnaryOperator<Cuboid> operator = area != null ? area::intersection : c -> c;
        return set.stream().map(operator).mapToLong(Cuboid::size).sum();
    }

    private static record Cuboid(int x1, int x2, int y1, int y2, int z1, int z2) {

        Cuboid(int[] v) {
            this(v[0], v[1], v[2], v[3], v[4], v[5]);
        }

        long size() {
            return Math.max(x2 - x1 + 1, 0L) * Math.max(y2 - y1 + 1, 0L) * Math.max(z2 - z1 + 1, 0L);
        }

        /**
         * Returns the intersection of this cuboid and the given cuboid. The result might be empty.
         */
        Cuboid intersection(Cuboid other) {
            return new Cuboid(Math.max(x1, other.x1), Math.min(x2, other.x2),
                    Math.max(y1, other.y1), Math.min(y2, other.y2),
                    Math.max(z1, other.z1), Math.min(z2, other.z2));
        }

        /**
         * Returns a list of disjoint cuboids obtained by removing the given cuboid from this cuboid.
         */
        List<Cuboid> remove(Cuboid other) {
            Cuboid in = intersection(other);
            if (in.size() == 0) {
                return List.of(this);
            }

            var result = new ArrayList<Cuboid>();
            for (int[] x : new int[][] { { x1, in.x1 - 1 }, { in.x1, in.x2 }, { in.x2 + 1, x2 } }) {
                for (int[] y : new int[][] { { y1, in.y1 - 1 }, { in.y1, in.y2 }, { in.y2 + 1, y2 } }) {
                    for (int[] z : new int[][] { { z1, in.z1 - 1 }, { in.z1, in.z2 }, { in.z2 + 1, z2 } }) {
                        var c = new Cuboid(x[0], x[1], y[0], y[1], z[0], z[1]);
                        if (c.size() > 0 && !in.equals(c)) {
                            result.add(c);
                        }
                    }
                }
            }

            return result;
        }

    }

}
