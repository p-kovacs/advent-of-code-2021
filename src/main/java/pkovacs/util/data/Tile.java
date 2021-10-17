package pkovacs.util.data;

import java.util.List;
import java.util.function.Predicate;

/**
 * Represents a tile (or cell) in a table or matrix as an immutable pair of {@code int} values: row index and column
 * index. Provides methods to get the neighbors of a tile and the Manhattan distance between two tiles.
 * <p>
 * This class is similar to {@link Point} but with different index order and names.
 *
 * @see Point
 */
public record Tile(int row, int col) {

    /**
     * Returns true if the indices of this tile are valid with respect to the given row and column count.
     *
     * @return true if the indices are between zero (inclusive) and the given row/column count (exclusive).
     */
    public boolean isValid(int rowCount, int colCount) {
        return row >= 0 && row < rowCount && col >= 0 && col < colCount;
    }

    /**
     * Returns the neighbor in the given direction. The tile {@code (0,0)} represents the upper-left corner among the
     * tiles with non-negative indices. The directions are interpreted accordingly, so "north" or "up" means lower
     * row index, while "south" or "down" means higher row index.
     */
    public Tile neighbor(Direction dir) {
        return switch (dir) {
            case NORTH -> new Tile(row - 1, col);
            case EAST -> new Tile(row, col + 1);
            case SOUTH -> new Tile(row + 1, col);
            case WEST -> new Tile(row, col - 1);
        };
    }

    /**
     * Returns the neighbor in the given direction. The tile {@code (0,0)} represents the upper-left corner among the
     * tiles with non-negative indices. The directions are interpreted accordingly, so "north" or "up" means lower
     * row index, while "south" or "down" means higher row index.
     *
     * @param dir the direction. One of 'N' (north), 'E' (east), 'S' (south), 'W' (west),
     *         'U' (up), 'R' (right), 'D' (down), 'L' (left), and their lowercase variants.
     */
    public Tile neighbor(char dir) {
        return neighbor(Direction.fromChar(dir));
    }

    /**
     * Returns the four neighbors of this tile.
     *
     * @return the four neighbor tiles in clockwise order (N, E, S, W)
     */
    public List<Tile> neighbors() {
        return List.of(
                new Tile(row - 1, col),
                new Tile(row, col + 1),
                new Tile(row + 1, col),
                new Tile(row, col - 1));
    }

    /**
     * Returns the neighbors of this tile that are accepted by the given predicate.
     *
     * @return the accepted neighbors (at most four tiles) in clockwise order (N, E, S, W)
     */
    public List<Tile> neighbors(Predicate<Tile> predicate) {
        return neighbors().stream().filter(predicate).toList();
    }

    /**
     * Returns the {@link #isValid(int, int) valid} neighbors of this tile with respect to the given row count and
     * column count.
     *
     * @return the valid neighbors (at most four tiles) in clockwise order (N, E, S, W)
     */
    public List<Tile> validNeighbors(int rowCount, int colCount) {
        return neighbors(t -> t.isValid(rowCount, colCount));
    }

    /**
     * Returns the eight "extended" neighbors of this tile, including the diagonal ones.
     *
     * @return the eight "extended" neighbor tiles in clockwise order (N, NE, E, SE, S, SW, W, NW)
     */
    public List<Tile> extendedNeighbors() {
        return List.of(
                new Tile(row - 1, col),
                new Tile(row - 1, col + 1),
                new Tile(row, col + 1),
                new Tile(row + 1, col + 1),
                new Tile(row + 1, col),
                new Tile(row + 1, col - 1),
                new Tile(row, col - 1),
                new Tile(row - 1, col - 1));
    }

    /**
     * Returns the "extended" neighbors of this tile that are accepted by the given predicate, including diagonal ones.
     *
     * @return the accepted "extended" neighbors (at most eight tiles) in clockwise order (N, NE, E, SE, S, SW, W, NW)
     */
    public List<Tile> extendedNeighbors(Predicate<Tile> predicate) {
        return extendedNeighbors().stream().filter(predicate).toList();
    }

    /**
     * Returns the Manhattan distance between this tile and the given tile.
     */
    public int dist(Tile t) {
        return dist(this, t);
    }

    /**
     * Returns the Manhattan distance between the given two tiles.
     */
    public static int dist(Tile t1, Tile t2) {
        return Math.abs(t1.row - t2.row) + Math.abs(t1.col - t2.col);
    }

}
