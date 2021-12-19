package pkovacs.aoc.y2021;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import pkovacs.aoc.AocUtils;
import pkovacs.util.InputUtils;

public class Day19 {

    private static final int OVERLAP_LIMIT = 12;

    public static void main(String[] args) {
        var input = InputUtils.readString(AocUtils.getInputPath());

        // Parse input data
        var blocks = InputUtils.collectLineBlocks(input);
        var data = new ArrayList<Set<Vector>>();
        var scanners = new ArrayList<Vector>();
        for (var block : blocks) {
            var set = block.subList(1, block.size()).stream()
                    .map(InputUtils::parseInts)
                    .map(Vector::new)
                    .collect(Collectors.toSet());
            data.add(set);
            scanners.add(new Vector(0, 0, 0));
        }

        // Start BFS from scanner 0 to reach other scanners and adjust their detection data accordingly
        var queue = new ArrayDeque<Integer>();
        queue.add(0);
        boolean[] adjusted = new boolean[data.size()];
        adjusted[0] = true;
        while (!queue.isEmpty()) {
            // Try to adjust other scanners' data to scanner i (which is already adjusted to scanner 0)
            int i = queue.remove();
            for (int j = 0; j < data.size(); j++) {
                if (!adjusted[j] && adjust(data, scanners, i, j)) {
                    adjusted[j] = true;
                    queue.add(j);
                }
            }
        }

        long beaconCount = data.stream().flatMap(Set::stream).distinct().count();
        long maxScannerDist = scanners.stream()
                .mapToLong(s1 -> scanners.stream().mapToLong(s1::dist).max().orElseThrow())
                .max().orElseThrow();

        System.out.println("Part 1: " + beaconCount);
        System.out.println("Part 2: " + maxScannerDist);
    }

    /**
     * Tries to adjust the data of scanner j to the data of scanner i, and returns true if the adjustment was
     * successful. If the two scanners don't have at least 12 overlapping beacons, this method returns false
     * without changing any data.
     */
    private static boolean adjust(List<Set<Vector>> data, List<Vector> scanners, int i, int j) {
        for (int r = 0; r < 24; r++) {
            int orientation = r;
            var set1 = data.get(i);
            var rotated = data.get(j).stream().map(v -> v.rotate(orientation)).toList();

            // Since at least 12 overlapping vectors are required, we can skip 11 vectors from the (rotated) vectors
            // corresponding to scanner j and match the others to each vector corresponding to scanner i
            var toAssign = rotated.subList(OVERLAP_LIMIT - 1, rotated.size());
            for (var v2 : toAssign) {
                for (var v1 : set1) {
                    var delta = v1.sub(v2);
                    long overlapSize = rotated.stream().map(delta::add).filter(set1::contains).count();
                    if (overlapSize >= OVERLAP_LIMIT) {
                        data.set(j, rotated.stream().map(delta::add).collect(Collectors.toSet()));
                        scanners.set(j, scanners.get(j).rotate(orientation).add(delta));
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Represents a 3D a position vector. This is a simplified version of {@link pkovacs.util.data.Vector},
     * extended with the the {@link #rotate(int)} method, which is essential for this puzzle.
     */
    private static record Vector(int x, int y, int z) {

        Vector(int[] array) {
            this(array[0], array[1], array[2]);
        }

        Vector add(Vector v) {
            return new Vector(x + v.x, y + v.y, z + v.z);
        }

        Vector sub(Vector v) {
            return new Vector(x - v.x, y - v.y, z - v.z);
        }

        long dist(Vector v) {
            return Math.abs(x - v.x) + Math.abs(y - v.y) + Math.abs(z - v.z);
        }

        /**
         * Rotates this vector according to the given orientation, which is an integer between 0 and 23 (inclusive).
         */
        Vector rotate(int orientation) {
            // Select "facing" of the first axis: positive or negative of the original x, y, or z. Note that if we
            // negate a coordinate (reverse an axis), then the other two coordinates must be swapped (or one of them
            // must be negated).
            Vector v = switch (orientation % 6) {
                case 0 -> new Vector(x(), y(), z());
                case 1 -> new Vector(-x(), z(), y());
                case 2 -> new Vector(y(), z(), x());
                case 3 -> new Vector(-y(), x(), z());
                case 4 -> new Vector(z(), x(), y());
                case 5 -> new Vector(-z(), y(), x());
                default -> throw new IllegalArgumentException("Orientation must be between 0 and 23 (inclusive).");
            };

            // Select rotation around the first axis: 0, 1, 2, 3 means rotating to the right by 0, 90, 180, 270
            // degrees.
            return switch (orientation / 6) {
                case 0 -> new Vector(v.x(), v.y(), v.z());
                case 1 -> new Vector(v.x(), v.z(), -v.y());
                case 2 -> new Vector(v.x(), -v.y(), -v.z());
                case 3 -> new Vector(v.x(), -v.z(), v.y());
                default -> throw new IllegalArgumentException("Orientation must be between 0 and 23 (inclusive).");
            };
        }

    }

}
