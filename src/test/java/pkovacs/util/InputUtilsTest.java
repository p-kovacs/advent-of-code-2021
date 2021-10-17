package pkovacs.util;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InputUtilsTest {

    @Test
    void testCollectLineBlocks() {
        String input = "a\nb c d\ne\n\n\n\nf g\nh\n\ni j k";
        var blocks = InputUtils.collectLineBlocks(input);

        assertEquals(3, blocks.size());
        assertEquals(List.of("a", "b c d", "e"), blocks.get(0));
        assertEquals(List.of("f g", "h"), blocks.get(1));
        assertEquals(List.of("i j k"), blocks.get(2));

        assertEquals(3, InputUtils.collectLineBlocks(input + "\n").size());
        assertEquals(3, InputUtils.collectLineBlocks(input + "\n\n\n\n").size());

        String inputWin = "a\r\nb c d\r\ne\r\n\r\nf g\r\nh\r\n\r\ni j k";
        var blocks2 = InputUtils.collectLineBlocks(inputWin);
        assertEquals(blocks, blocks2);
    }

    @Test
    void testParseIntegers() {
        String input = "I have 5 apples and 12 bananas. -42 is the opposite of 42.";
        assertArrayEquals(new int[] { 5, 12, -42, 42 }, InputUtils.parseInts(input));
        assertArrayEquals(new long[] { 5, 12, -42, 42 }, InputUtils.parseLongs(input));
    }

    @Test
    void testScan() {
        var values = InputUtils.scan("Product PID_4242X is ordered.", ".*PID_%d%c is %s[.]");

        assertEquals(3, values.size());
        assertTrue(values.get(0).isInteger());
        assertEquals(4242, values.get(0).asInt());
        assertTrue(values.get(1).isChar());
        assertEquals('X', values.get(1).asChar());
        assertTrue(values.get(2).isString());
        assertEquals("ordered", values.get(2).get());

        assertEquals("[ParsedValue(Long: 4242), ParsedValue(Character: X), ParsedValue(String: ordered)]",
                values.toString());
    }

}
