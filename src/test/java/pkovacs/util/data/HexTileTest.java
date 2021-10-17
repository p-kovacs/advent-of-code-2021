package pkovacs.util.data;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HexTileTest {

    @Test
    void test() {
        var c = new HexTile(12, 42);
        var d = new HexTile(42, 12);
        var e = new HexTile(42, 12);

        assertEquals(12, c.row());
        assertEquals(42, c.col());
        assertNotEquals(c, d);
        assertEquals(d, e);

        assertEquals(c, c.gotoTile("nwwswee"));
        assertEquals(c, c.gotoTile("NWwswEe"));
        assertEquals(new HexTile(13, 42), c.gotoTile("ESEW"));

        assertThrows(IllegalArgumentException.class, () -> c.neighbor("n"));
        assertThrows(IllegalArgumentException.class, () -> c.neighbor("u"));
        assertThrows(IllegalArgumentException.class, () -> c.gotoTile("nwwsnee"));
        assertThrows(IllegalArgumentException.class, () -> c.gotoTile("nwwees"));
        assertThrows(IllegalArgumentException.class, () -> c.gotoTile("nW"));

        assertEquals(List.of(
                        new HexTile(11, 43),
                        new HexTile(12, 43),
                        new HexTile(13, 42),
                        new HexTile(13, 41),
                        new HexTile(12, 41),
                        new HexTile(11, 42)),
                c.neighbors());
    }

}
