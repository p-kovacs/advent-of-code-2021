package pkovacs.aoc.y2021;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import pkovacs.aoc.AocUtils;
import pkovacs.util.InputUtils;
import pkovacs.util.alg.Dijkstra;
import pkovacs.util.alg.Dijkstra.Edge;
import pkovacs.util.data.CharTable;
import pkovacs.util.data.Tile;

public class Day23 {

    private static final int HALLWAY_ROW = 1;

    private static final Set<Tile> ENTRANCES =
            Set.of(new Tile(1, 3), new Tile(1, 5), new Tile(1, 7), new Tile(1, 9));

    private static final long INFEASIBLE_ENERGY = 1_000_000;

    public static void main(String[] args) {
        var input = InputUtils.readLines(AocUtils.getInputPath());

        System.out.println("Part 1: " + solve(input, false));
        System.out.println("Part 2: " + solve(input, true));
    }

    /**
     * Finds the solution as the length of the shortest path from the starting configuration to the required
     * configuration. The configurations are represented by {@link State} objects, and each feasible step
     * (a directed edge in the graph) moves a letter (A, B, C, or D) to an empty position (.) that is one or more
     * tiles away. For example, a single feasible step can move a letter 'B' up, left, left, and down using 40 energy.
     */
    private static long solve(List<String> input, boolean extended) {
        var start = initState(input, extended);
        return Dijkstra.findPath(start, State::collectMoves, State::isOrganized).orElseThrow().dist();
    }

    private static State initState(List<String> input, boolean extended) {
        var lines = new ArrayList<>(input);
        if (extended) {
            lines.add(3, "  #D#C#B#A#");
            lines.add(4, "  #D#B#A#C#");
        }
        return new State(lines);
    }

    /**
     * Represents a state of the puzzle as a {@link CharTable} with additional methods.
     */
    private static class State extends CharTable {

        State(List<String> lines) {
            super(lines.size(), lines.get(0).length(),
                    (i, j) -> j < lines.get(i).length() ? lines.get(i).charAt(j) : ' ');
        }

        State(State other) {
            super(other);
        }

        /**
         * Collects feasible moves (directed edges of the graph) from the given state.
         */
        List<Edge<State>> collectMoves() {
            var edges = new ArrayList<Edge<State>>();

            // Collect feasible moves
            var toTiles = toTiles();
            for (var from : fromTiles()) {
                for (var to : toTiles) {
                    long energy = calculateEnergy(from, to);
                    if (energy < INFEASIBLE_ENERGY) {
                        edges.add(new Edge<>(move(from, to), energy));
                    }
                }
            }

            return edges;
        }

        List<Tile> fromTiles() {
            return cells(1, 1, rowCount() - 1, colCount() - 1)
                    .filter(t -> get(t) >= 'A' && get(t) <= 'D')
                    .filter(this::canBeMoved)
                    .toList();
        }

        List<Tile> toTiles() {
            return cells(1, 1, rowCount() - 1, colCount() - 1)
                    .filter(t -> get(t) == '.' && !ENTRANCES.contains(t))
                    .toList();
        }

        State move(Tile from, Tile to) {
            var newState = new State(this);
            newState.set(from, get(to));
            newState.set(to, get(from));
            return newState;
        }

        boolean canBeMoved(Tile from) {
            // A letter can be moved if it is on the hallway or at the top of the "stack" of a room, unless all
            // letters in that room are already organized
            var above = new Tile(from.row() - 1, from.col());
            return from.row() == HALLWAY_ROW || (isEmpty(above) && !isOrganizedBelow(above));
        }

        long calculateEnergy(Tile from, Tile to) {
            char letter = get(from);

            // Moving from hallway to hallway is not allowed
            if (from.row() == HALLWAY_ROW && to.row() == HALLWAY_ROW) {
                return INFEASIBLE_ENERGY;
            }

            // Do not move within the same column (room)
            if (from.col() == to.col()) {
                return INFEASIBLE_ENERGY;
            }

            // Do not move a letter to a room if it is not the appropriate room or if not all tiles below the target
            // tile are organized properly
            if (to.row() != HALLWAY_ROW && (to.col() != column(letter) || !isOrganizedBelow(to))) {
                return INFEASIBLE_ENERGY;
            }

            // Traverse the path of the letter (up, left/right, down) to calculate the energy requirement of this move.
            // If there is another letter blocking the way, an "infeasible" energy requirement is returned.
            int unit = energy(letter);
            int energy = 0;
            for (int i = from.row() - 1; i >= 1; i--) {
                energy += isEmpty(i, from.col()) ? unit : INFEASIBLE_ENERGY;
            }
            for (int j = Math.min(from.col() + 1, to.col()); j <= Math.max(to.col(), from.col() - 1); j++) {
                energy += isEmpty(1, j) ? unit : INFEASIBLE_ENERGY;
            }
            for (int i = 2; i <= to.row(); i++) {
                energy += isEmpty(i, to.col()) ? unit : INFEASIBLE_ENERGY;
            }

            return energy;
        }

        boolean isEmpty(int i, int j) {
            return get(i, j) == '.';
        }

        boolean isEmpty(Tile t) {
            return get(t) == '.';
        }

        boolean isOrganized() {
            return ENTRANCES.stream().allMatch(this::isOrganizedBelow);
        }

        boolean isOrganizedBelow(Tile tile) {
            return IntStream.range(tile.row() + 1, rowCount() - 1)
                    .allMatch(j -> column(get(j, tile.col())) == tile.col());
        }

        static int column(char letter) {
            return switch (letter) {
                case 'A' -> 3;
                case 'B' -> 5;
                case 'C' -> 7;
                case 'D' -> 9;
                default -> -1;
            };
        }

        static int energy(char letter) {
            return switch (letter) {
                case 'A' -> 1;
                case 'B' -> 10;
                case 'C' -> 100;
                case 'D' -> 1000;
                default -> throw new IllegalArgumentException();
            };
        }

    }

}
