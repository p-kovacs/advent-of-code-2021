package com.github.pkovacs.util.data;

import java.util.List;

/**
 * Represents a hexagonal tile as an immutable pair of int values: row index and column index.
 * Provides methods to get the neighbors of a tile.
 * <p>
 * In this representation, a row corresponds to the west-east direction, while a column corresponds to the
 * northwest-southeast direction. That is, the neighbors of a hexagonal tile {@code (r, c)} in directions
 * "NW" and "SE" are {@code (r - 1, c)} and {@code (r + 1, c)}, respectively.
 */
public record HexTile(int row, int col) {

    /**
     * Returns the six neighbors of this tile.
     *
     * @return the six neighbor tiles in clockwise order (NE, E, SE, SW, W, NW).
     */
    public List<HexTile> neighbors() {
        return List.of(
                new HexTile(row - 1, col + 1),
                new HexTile(row, col + 1),
                new HexTile(row + 1, col),
                new HexTile(row + 1, col - 1),
                new HexTile(row, col - 1),
                new HexTile(row - 1, col));
    }

    /**
     * Returns the neighbor in the given direction.
     *
     * @param direction the direction. One of "W" (west), "E" (east), "NW" (northwest), "NE" (northeast),
     *         "SW" (southwest), "SE" (southeast), and their lowercase variants.
     */
    public HexTile neighbor(String direction) {
        return switch (direction) {
            case "W", "w" -> new HexTile(row, col - 1);
            case "E", "e" -> new HexTile(row, col + 1);
            case "NW", "nw" -> new HexTile(row - 1, col);
            case "NE", "ne" -> new HexTile(row - 1, col + 1);
            case "SW", "sw" -> new HexTile(row + 1, col - 1);
            case "SE", "se" -> new HexTile(row + 1, col);
            default -> throw new IllegalArgumentException("Unknown direction: \"" + direction + "\".");
        };
    }

    /**
     * Returns the tile reached by following the given path from this tile.
     *
     * @param path The path string, a sequence of direction identifiers without separators.
     *         For example, {@code "nwwswee"} or {@code "ESESW"} . See {@link #neighbor(String)} for the list of
     *         valid directions.
     */
    public HexTile gotoTile(String path) {
        HexTile tile = this;
        for (int i = 0; i < path.length(); i++) {
            char ch = path.charAt(i);
            if (ch == 'n' || ch == 'N' || ch == 's' || ch == 'S') {
                tile = tile.neighbor(path.substring(i, Math.min(i + 2, path.length())));
                i++;
            } else {
                tile = tile.neighbor(path.substring(i, i + 1));
            }
        }
        return tile;
    }

}
