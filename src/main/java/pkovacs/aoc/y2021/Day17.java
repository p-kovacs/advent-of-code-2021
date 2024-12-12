package pkovacs.aoc.y2021;

import pkovacs.util.InputUtils;
import pkovacs.util.data.Point;

public class Day17 {

    public static void main(String[] args) {
        var ints = InputUtils.readInts(AocUtils.getInputPath());

        int minX = ints[0];
        int maxX = ints[1];
        int minY = ints[2];
        int maxY = ints[3];

        // Check assumptions for the coordinates of the target area: x is positive, y is negative
        if (minX <= 0 || maxY >= 0) {
            throw new IllegalArgumentException();
        }

        // Simple approach: try each initial aim (velocity) within a reasonable range ("rectangle").
        // For the x coordinate, [1..maxX] is enough, but the accurate range is [i..maxX], where i is the smallest
        // integer for which i*(i+1)/2 >= minX.
        // For the y coordinate, we need [minY..j] for some j. For the upper bound, note that if the y coordinate
        // of the initial aim is a positive number k, then the y coordinate of probe will eventually be exactly 0
        // with the y coordinate of the current aim being equal to -k, so if we want to avoid jumping through
        // the target area for the next step (i.e., k+1 downwards), k < -minY is required. So the range to check is
        // [minY..(-minY - 1)].
        int maxHeight = Integer.MIN_VALUE;
        int count = 0;
        for (int x = 1; x <= maxX; x++) {
            if (x * (x + 1) / 2 < minX) {
                continue;
            }
            for (int y = minY; y <= -minY - 1; y++) {
                var aim = new Point(x, y);
                var p = new Point(0, 0);
                int height = Integer.MIN_VALUE;
                while (p.x() <= maxX && p.y() >= minY) {
                    if (p.x() >= minX && p.y() <= maxY) {
                        maxHeight = Math.max(maxHeight, height);
                        count++;
                        break;
                    }
                    height = Math.max(height, p.y());
                    p = new Point(p.x() + aim.x(), p.y() + aim.y());
                    aim = new Point(Math.max(aim.x() - 1, 0), aim.y() - 1);
                }
            }
        }

        System.out.println("Part 1: " + maxHeight);
        System.out.println("Part 2: " + count);
    }

}
