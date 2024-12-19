package com.github.pkovacs.aoc.y2021;

import java.util.ArrayList;
import java.util.List;

import com.github.pkovacs.util.InputUtils;

public class Day16 {

    public static void main(String[] args) {
        var input = InputUtils.readFirstLine(AocUtils.getInputPath());

        var packet = parse(convertToBinary(input));

        System.out.println("Part 1: " + sumVersions(packet));
        System.out.println("Part 2: " + evaluate(packet));
    }

    private record Packet(int version, int type, int endIndex, long value, List<Packet> operands) {}

    private static String convertToBinary(String input) {
        var sb = new StringBuilder();
        for (var c : input.toCharArray()) {
            var binary = Integer.toBinaryString(InputUtils.parseInt(c));
            sb.append("0000", 0, 4 - binary.length()).append(binary);
        }
        return sb.toString();
    }

    private static Packet parse(String s) {
        int version = Integer.parseInt(s.substring(0, 3), 2);
        int type = Integer.parseInt(s.substring(3, 6), 2);
        long value = -1;
        int endIndex;

        var operands = new ArrayList<Packet>();
        if (type == 4) {
            // Parse literal value
            var sb = new StringBuilder();
            endIndex = 6;
            do {
                sb.append(s, endIndex + 1, endIndex + 5);
                endIndex += 5;
            } while (s.charAt(endIndex - 5) == '1');
            value = Long.parseLong(sb.toString(), 2);
        } else {
            // Parse operator
            if (s.charAt(6) == '0') {
                int length = Integer.parseInt(s.substring(7, 22), 2);
                endIndex = 22;
                while (endIndex < 22 + length) {
                    var p = parse(s.substring(endIndex));
                    operands.add(p);
                    endIndex += p.endIndex;
                }
            } else {
                int operandCount = Integer.parseInt(s.substring(7, 18), 2);
                endIndex = 18;
                while (operands.size() < operandCount) {
                    var p = parse(s.substring(endIndex));
                    operands.add(p);
                    endIndex += p.endIndex;
                }
            }
        }

        return new Packet(version, type, endIndex, value, operands);
    }

    private static long sumVersions(Packet p) {
        return p.version + p.operands.stream().mapToLong(Day16::sumVersions).sum();
    }

    private static long evaluate(Packet p) {
        return switch (p.type) {
            case 4 -> p.value;
            case 0 -> p.operands.stream().mapToLong(Day16::evaluate).sum();
            case 1 -> p.operands.stream().mapToLong(Day16::evaluate).reduce(1, (a, b) -> a * b);
            case 2 -> p.operands.stream().mapToLong(Day16::evaluate).min().orElseThrow();
            case 3 -> p.operands.stream().mapToLong(Day16::evaluate).max().orElseThrow();
            case 5 -> evaluate(p.operands.get(0)) > evaluate(p.operands.get(1)) ? 1 : 0;
            case 6 -> evaluate(p.operands.get(0)) < evaluate(p.operands.get(1)) ? 1 : 0;
            case 7 -> evaluate(p.operands.get(0)) == evaluate(p.operands.get(1)) ? 1 : 0;
            default -> throw new IllegalArgumentException();
        };
    }

}
