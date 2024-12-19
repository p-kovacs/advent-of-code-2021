package com.github.pkovacs.aoc.y2021;

import java.util.ArrayList;
import java.util.List;

import com.github.pkovacs.util.InputUtils;

public class Day18 {

    public static void main(String[] args) {
        var lines = InputUtils.readLines(AocUtils.getInputPath());

        System.out.println("Part 1: " + solve1(lines));
        System.out.println("Part 2: " + solve2(lines));
    }

    private static long solve1(List<String> lines) {
        Number sum = null;
        for (var line : lines) {
            var x = Number.parse(line);
            sum = sum == null ? x : Number.add(sum, x);
        }
        return sum.magnitude();
    }

    private static long solve2(List<String> lines) {
        long max = -1;
        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < lines.size(); j++) {
                if (i != j) {
                    var sum = Number.add(Number.parse(lines.get(i)), Number.parse(lines.get(j)));
                    max = Math.max(max, sum.magnitude());
                }
            }
        }
        return max;
    }

    /**
     * Represents a "snailfish number" as a binary tree. A {@link Number} is either "simple" (a leaf in the tree) or
     * have two children, which are also {@link Number} objects. A simple {@link Number} instance stores a long value
     * and have no children.
     */
    static class Number {

        private long value;
        private Number left;
        private Number right;

        Number(long value) {
            this(value, null, null);
        }

        Number(long value, Number left, Number right) {
            this.value = value;
            this.left = left;
            this.right = right;
        }

        boolean isSimple() {
            return left == null; // (left == null) if and only if (right == null)
        }

        static Number parse(String s) {
            if (!s.contains(",")) {
                return new Number(Long.parseLong(s));
            }

            var parts = split(s);
            return new Number(0, parse(parts.get(0)), parse(parts.get(1)));
        }

        private static List<String> split(String s) {
            int nested = 0;
            int pos = -1;
            for (int i = 1; i < s.length() - 1 && pos < 0; i++) {
                switch (s.charAt(i)) {
                    case '[' -> nested++;
                    case ']' -> nested--;
                    case ',' -> pos = nested == 0 ? i : -1;
                }
            }
            return List.of(s.substring(1, pos), s.substring(pos + 1, s.length() - 1));
        }

        long magnitude() {
            return isSimple() ? value : 3 * left.magnitude() + 2 * right.magnitude();
        }

        static Number add(Number a, Number b) {
            var sum = new Number(0, a, b);
            sum.reduce();
            return sum;
        }

        private void reduce() {
            while (explode() || split()) {
            }
        }

        private boolean explode() {
            var x = findToExplode(this, 0);
            if (x != null) {
                explode(x);
            }
            return x != null;
        }

        private boolean split() {
            var x = findToSplit(this);
            if (x != null) {
                split(x);
            }
            return x != null;
        }

        private static Number findToExplode(Number x, int depth) {
            if (x.isSimple()) {
                return null;
            } else if (x.left.isSimple() && x.right.isSimple()) {
                return depth == 4 ? x : null;
            } else {
                return select(findToExplode(x.left, depth + 1), findToExplode(x.right, depth + 1));
            }
        }

        private void explode(Number x) {
            var list = new ArrayList<Number>();
            inorder(this, list);

            int i = list.indexOf(x.left);
            if (i > 0) {
                list.get(i - 1).value += x.left.value;
            }
            if (i < list.size() - 2) {
                list.get(i + 2).value += x.right.value;
            }

            x.value = 0;
            x.left = null;
            x.right = null;
        }

        private static void inorder(Number x, List<Number> list) {
            if (x.isSimple()) {
                list.add(x);
            } else {
                inorder(x.left, list);
                inorder(x.right, list);
            }
        }

        private static Number findToSplit(Number x) {
            if (x.isSimple()) {
                return x.value >= 10 ? x : null;
            } else {
                return select(findToSplit(x.left), findToSplit(x.right));
            }
        }

        private static void split(Number x) {
            x.left = new Number(x.value / 2);
            x.right = new Number(x.value - x.value / 2);
            x.value = 0;
        }

        private static Number select(Number x, Number y) {
            return x != null ? x : y;
        }

        @Override
        public String toString() {
            return isSimple() ? String.valueOf(value) : "[" + left + "," + right + "]";
        }

    }

}
