package pkovacs.util.data;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CharTableTest extends AbstractTableTest<Character> {

    @Test
    void testGettersAndSetters() {
        var table = new CharTable(3, 4, ' ');

        assertEquals(3, table.rowCount());
        assertEquals(4, table.colCount());

        assertContentEquals(List.of("    ", "    ", "    "), table);

        var table2 = createTestTable(3, 4);
        table.cells().forEach(t -> table.set(t, table2.get(t))); // deliberately copied this way to test get-set

        assertContentEquals(List.of("0123", "abcd", "ABCD"), table);

        table.cells().forEach(t -> table.update(t, c -> (char) (c + 1)));
        table.set(0, 0, '#');
        table.set(new Tile(2, 2), '@');

        assertContentEquals(List.of("#234", "bcde", "BC@E"), table);

        table.fill('x');

        assertContentEquals(List.of("xxxx", "xxxx", "xxxx"), table);
    }

    @Test
    void testStreamMethods() {
        var table = createTestTable(3, 4);

        assertEquals(table.values().toList(), table.cells().map(table::get).toList());
        assertEquals(table.rowValues(1).toList(), table.row(1).map(table::get).toList());
        assertEquals(table.colValues(2).toList(), table.col(2).map(table::get).toList());
    }

    @Test
    void testToString() {
        var table = createTestTable(3, 4);

        assertEquals("0123\nabcd\nABCD\n", table.toString());
    }

    private static void assertContentEquals(List<String> expected, CharTable table) {
        var array = new char[expected.size()][];
        for (int i = 0; i < array.length; i++) {
            array[i] = expected.get(i).toCharArray();
        }
        assertTrue(Arrays.deepEquals(array, table.asArray()));
    }

    @Override
    CharTable createTestTable(int rowCount, int colCount) {
        Function<Integer, Character> baseChar = i -> switch (i) {
            case 0 -> '0';
            case 1 -> 'a';
            default -> 'A';
        };
        return new CharTable(rowCount, colCount, (r, c) -> (char) (baseChar.apply(r % 3) + c));
    }

}
