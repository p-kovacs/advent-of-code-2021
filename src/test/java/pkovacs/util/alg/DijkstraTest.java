package pkovacs.util.alg;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import pkovacs.util.alg.Dijkstra.Edge;

class DijkstraTest extends AbstractShortestPathTest {

    @Override
    <T> Optional<PathResult<T>> findPath(T source,
            Function<? super T, ? extends Iterable<Edge<T>>> edgeProvider,
            Predicate<? super T> targetPredicate) {
        return Dijkstra.findPath(source, edgeProvider, targetPredicate);
    }

}
