package pkovacs.util.alg;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import pkovacs.util.InputUtils;
import pkovacs.util.alg.Dijkstra.Edge;
import pkovacs.util.data.CharTable;
import pkovacs.util.data.Tile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

abstract class AbstractShortestPathTest {

    abstract <T> Optional<PathResult<T>> findPath(T source,
            Function<? super T, ? extends Iterable<Edge<T>>> edgeProvider,
            Predicate<? super T> targetPredicate);

    @Test
    void findPathInMaze() {
        // We have to find the shortest path in a maze from the top left tile to the bottom right tile.
        // Walls should be bypassed or "blown up", but it takes detonationTime seconds to blow up a single wall
        // tile next to the current tile and step into its location, while a single step to an adjacent empty
        // tile takes only 1 second.
        // See maze.txt, '#' represents a wall tile, '.' represents an empty tile.

        var input = InputUtils.readCharMatrix(InputUtils.getPath(getClass(), "maze.txt"));
        var maze = new CharTable(input);
        var start = new Tile(0, 0);
        var end = new Tile(maze.rowCount() - 1, maze.colCount() - 1);

        // Find path with large detonationTime --> same as BFS
        long detonationTime = 32;
        var result = findPathInMaze(maze, start, end, detonationTime);

        assertEquals(50, result.dist());
        assertEquals(51, result.path().size());
        assertEquals(start, result.path().get(0));
        assertEquals(end, result.path().get(result.path().size() - 1));

        // Find path with smaller detonationTime --> better than BFS
        detonationTime = 30;
        result = findPathInMaze(maze, start, end, detonationTime);

        assertEquals(49, result.dist());

        // Find path with detonationTime == 1 --> Manhattan distance
        detonationTime = 1;
        result = findPathInMaze(maze, start, end, detonationTime);

        assertEquals(20, result.dist());
        assertEquals(start.dist(end), result.dist());
    }

    private PathResult<Tile> findPathInMaze(CharTable maze, Tile start, Tile end, long detonationTime) {
        var result = findPath(start,
                tile -> maze.neighborCells(tile)
                        .map(t -> new Edge<>(t, maze.get(t) == '.' ? 1 : detonationTime))
                        .toList(),
                end::equals);

        assertTrue(result.isPresent());
        return result.get();
    }

    @Test
    void testGenericParameters() {
        Function<Collection<Integer>, Collection<Edge<List<Integer>>>> neighborProvider = c ->
                IntStream.rangeClosed(0, 3)
                        .mapToObj(i -> new Edge<>(concat(c, i).toList(), Math.max(i * 10, 1)))
                        .filter(e -> e.endNode().size() <= 10)
                        .toList();

        var start = List.of(1, 0);
        var target = List.of(1, 0, 1, 0, 0, 1, 2);
        Predicate<Collection<Integer>> predicate = target::equals;

        var path = ShortestPath.findPath(start, neighborProvider, predicate);

        assertTrue(path.isPresent());
        assertEquals(42, path.get().dist());
        assertEquals(target, path.get().node());
    }

    private static Stream<Integer> concat(Collection<Integer> collection, int i) {
        return Stream.concat(collection.stream(), Stream.of(i));
    }

}
