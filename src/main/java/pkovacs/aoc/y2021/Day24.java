package pkovacs.aoc.y2021;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import pkovacs.util.InputUtils;

public class Day24 {

    public static void main(String[] args) {
        var lines = InputUtils.readLines(AocUtils.getInputPath());

        System.out.println("Part 1: " + solve(lines, false));
        System.out.println("Part 2: " + solve(lines, true));
    }

    /**
     * The key to solve this puzzle is to understand the code in the input. It consists of 14 "blocks" beginning
     * with the commands {@code inp w}. Each block processes the next digit of the input and essentially manipulates
     * the value in z in radix 26. (Variables w, x, and y are only used to store temporary values, and they are
     * always reinitialized in each block.)
     * <p>
     * More precisely, each block is equivalent to these lines:
     * <pre>
     *     w = nextDigit();
     *     x = z % 26 + B == w ? 0 : 1;
     *     z = z / A;
     *     z = z * (25 * x + 1) + (w + C) * x;
     * </pre>
     * where A, B, C are constants occurring in the code block. The last line can also be written this way:
     * {@code z = x == 1 ? z * 26 + (w + C) : z}.
     * <p>
     * The constant A is always 1 or 26, and C is always non-negative. Moreover, if A == 1, then B is always greater
     * than 9, and if A == 26, then B is always negative. That is, in a block where A == 1, x is always 1, so a new
     * non-zero digit is added to z (in radix 26). On the other hand, in a block where A == 26, x can be either 0 or 1.
     * So it either removes or replaces the last digit of z (in radix 26), respectively.
     * <p>
     * Furthermore, 7 out of the 14 blocks have A == 1, the others have A == 26. So the blocks where A == 1 always
     * add 7 non-zero digits to z (in radix 26). That is, in order to have a final result z == 0, each block where
     * A == 26 must remove a digit from z. Therefore, each input digit position with A == 1 block should have a
     * corresponding input digit position with A == 26 block such that the second block removes the digit in z
     * that was introduced by the first block.
     * <p>
     * Let w1, A1, B1, C1, w2, A2, B2, C2 denote the input digits and constants in two corresponding blocks, where
     * A1 == 1 and A2 == 26. To have x == 0 in the second block, w1 + C1 + B2 == w2 is required (while B1 and C2
     * are not relevant). That is, the difference between the digits w2 and w1 must be C1 + B2.
     */
    private static String solve(List<String> lines, boolean min) {
        // Find constants
        var divs = lines.stream()
                .filter(line -> line.startsWith("div z "))
                .map(line -> line.split(" ")[2])
                .map(Integer::parseInt)
                .toList();
        var shifts1 = lines.stream()
                .filter(line -> line.startsWith("add x ") && !line.equals("add x z"))
                .map(line -> line.split(" ")[2])
                .map(Integer::parseInt)
                .toList();
        var shifts2 = IntStream.range(1, lines.size())
                .filter(i -> lines.get(i).startsWith("add y ") && lines.get(i - 1).equals("add y w"))
                .mapToObj(i -> lines.get(i).split(" ")[2])
                .map(Integer::parseInt)
                .toList();

        // Find the pairs of matching positions of input digits and construct the min/max input that yields zero
        // result by identifying the feasible digit pair for each position pair
        var input = new int[14];
        var stack = new ArrayDeque<Integer>();
        for (int j = 0; j < 14; j++) {
            if (divs.get(j) == 1) {
                stack.add(j);
            } else {
                int i = stack.removeLast();
                int shift = shifts2.get(i) + shifts1.get(j);
                input[i] = min ? Math.max(1, 1 - shift) : Math.min(9, 9 - shift);
                input[j] = input[i] + shift;
            }
        }

        return Arrays.stream(input).mapToObj(String::valueOf).collect(Collectors.joining());
    }

}
