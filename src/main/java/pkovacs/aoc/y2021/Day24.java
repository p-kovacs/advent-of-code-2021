package pkovacs.aoc.y2021;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pkovacs.aoc.AocUtils;
import pkovacs.util.InputUtils;
import pkovacs.util.data.CounterMap;

public class Day24 {

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
     * {@code z = x == 1 ? z * 26 + w + C : z}.
     * <p>
     * The constant A is always 1 or 26, and C is always non-negative. Moreover, if A == 1, then B is always greater
     * than 9, and if A == 26, then B is always negative. That is, in a block where A == 1, x is always 1, so a new
     * non-zero digit is added to z (in radix 26). On the other hand, in a block where A == 26, x can be either 0 or 1.
     * So it either removes or replaces the last digit of z (in radix 26), respectively.
     * <p>
     * Furthermore, 7 out of the 14 blocks have A == 1, the others have A == 26. So the blocks where A == 1 always
     * add 7 non-zero digits to z (in radix 26). That is, in order to have a final result z == 0, each block where
     * A == 26 must remove a digit from z. Therefore, each input digit position with a corresponding block having
     * A == 1 will have a matching input digit position with A == 26 block. And for these 7 pairs of positions,
     * the digits must be selected in a way that the second block removes the digit in z (in radix 26) that was
     * previously added by the first block.
     */
    public static void main(String[] args) {
        var lines = InputUtils.readLines(AocUtils.getInputPath());

        // Find constants A
        var divs = lines.stream()
                .filter(line -> line.startsWith("div z "))
                .map(line -> line.split(" ")[2])
                .map(Integer::parseInt)
                .toList();

        // Find the pairs of matching positions of input digits
        var stack = new ArrayDeque<Integer>();
        var posPairs = new ArrayList<PosPair>();
        for (int i = 0; i < 14; i++) {
            if (divs.get(i) == 1) {
                stack.add(i);
            } else {
                posPairs.add(new PosPair(stack.removeLast(), i));
            }
        }

        // Find appropriate pairs of digits for pairs of digit positions
        var map = new HashMap<PosPair, List<DigitPair>>();
        for (int i = 0; i < posPairs.size(); i++) {
            // Find appropriate digits for this position pair (using validated digits for previous position pairs):
            // only the ones that result in the fewest digit count in the result
            var posPair = posPairs.get(i);
            var digitPairs = new ArrayList<DigitPair>();
            int minDigitCount = Integer.MAX_VALUE;
            for (char da = '1'; da <= '9'; da++) {
                for (char db = '1'; db <= '9'; db++) {
                    var array = "99999999999999".toCharArray();
                    array[posPair.a] = da;
                    array[posPair.b] = db;
                    for (int j = 0; j < i; j++) {
                        var prevPos = posPairs.get(j);
                        var prevDigits = map.get(prevPos).get(0);
                        array[prevPos.a] = prevDigits.a;
                        array[prevPos.b] = prevDigits.b;
                    }

                    int digitCount = runCode(lines, new String(array).substring(0, posPair.b + 1));

                    if (digitCount < minDigitCount) {
                        digitPairs.clear();
                        minDigitCount = digitCount;
                    }
                    if (digitCount == minDigitCount) {
                        digitPairs.add(new DigitPair(da, db));
                    }
                }
            }

            map.put(posPair, digitPairs);
        }

        // Construct min and max input values that yield zero result
        var min = "..............".toCharArray();
        var max = "..............".toCharArray();
        map.forEach((posPair, digitPairs) -> {
            min[posPair.a] = digitPairs.get(0).a;
            min[posPair.b] = digitPairs.get(0).b;
            max[posPair.a] = digitPairs.get(digitPairs.size() - 1).a;
            max[posPair.b] = digitPairs.get(digitPairs.size() - 1).b;
        });

        System.out.println("Part 1: " + new String(max));
        System.out.println("Part 2: " + new String(min));
    }

    private static record PosPair(int a, int b) {}

    private static record DigitPair(char a, char b) {}

    /**
     * Runs the given code for the given input and returns the number of digits of the value associated with the
     * variable z in radix 26.
     */
    private static int runCode(List<String> lines, String input) {
        var mem = new CounterMap<String>();
        int index = 0;

        // Executes the code for the given input
        loop:
        for (var line : lines) {
            var p = line.split(" ");
            switch (p[0]) {
                case "inp" -> {
                    if (index == input.length()) {
                        break loop;
                    }
                    mem.put(p[1], InputUtils.parseInt(input.charAt(index++)));
                }
                case "add" -> mem.put(p[1], value(mem, p[1]) + value(mem, p[2]));
                case "mul" -> mem.put(p[1], value(mem, p[1]) * value(mem, p[2]));
                case "div" -> mem.put(p[1], value(mem, p[1]) / value(mem, p[2]));
                case "mod" -> mem.put(p[1], value(mem, p[1]) % value(mem, p[2]));
                case "eql" -> mem.put(p[1], value(mem, p[1]) == value(mem, p[2]) ? 1 : 0);
            }
        }

        // Calculate the number of digits of z in radix 26
        long z = mem.getValue("z");
        int count = 0;
        while (z > 0) {
            z /= 26;
            count++;
        }
        return count;
    }

    private static long value(CounterMap<String> mem, String s) {
        return Character.isLetter(s.charAt(0)) ? mem.getValue(s) : Long.parseLong(s);
    }

}
