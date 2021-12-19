package dev.willebrands.mancala.game;

import static dev.willebrands.mancala.game.HouseStream.houseStream;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import dev.willebrands.mancala.game.HouseStream.HouseStreamBuilder;
import org.junit.jupiter.api.Test;

class HouseStreamTest {

    @Test
    void counterClockwiseStartIsExclusive() {
        HouseStreamBuilder houseStream = houseStream(2, 6).includeStores(false);
        HouseIdentifier start = new HouseIdentifier(0, 1);
        HouseIdentifier firstStreamElement =
                houseStream.counterClockwise().startAfter(start).stream().findFirst().orElseThrow();
        assertEquals(new HouseIdentifier(0, 2), firstStreamElement);
    }

    @Test
    void counterClockwiseLoopsThroughAllInOrder() {
        HouseStreamBuilder houseStreamBuilder = houseStream(2, 2).includeStores(false);
        HouseIdentifier start = new HouseIdentifier(0, 0);
        assertIterableEquals(
                asList(
                        new HouseIdentifier(0, 1),
                        new HouseIdentifier(1, 0),
                        new HouseIdentifier(1, 1),
                        new HouseIdentifier(0, 0)),
                houseStreamBuilder.counterClockwise().startAfter(start).stream()
                        .limit(4)
                        .collect(toList()));
    }

    @Test
    void storesCanBeIncluded() {
        HouseStreamBuilder houseStream = houseStream(2, 2).includeStores(true);
        HouseIdentifier start = new HouseIdentifier(0, 0);
        assertIterableEquals(
                asList(
                        new HouseIdentifier(0, 1),
                        new HouseIdentifier(0, 2),
                        new HouseIdentifier(1, 0),
                        new HouseIdentifier(1, 1)),
                houseStream.counterClockwise().startAfter(start).stream()
                        .limit(4)
                        .collect(toList()));
    }

    @Test
    void houseAfterPlayersLastIsStore() {
        HouseStreamBuilder houseStream = houseStream(2, 2).includeStores(true);
        HouseIdentifier start = new HouseIdentifier(0, 1);
        assertEquals(
                new HouseIdentifier(0, 2),
                houseStream.counterClockwise().startAfter(start).stream()
                        .findFirst()
                        .orElseThrow());
    }
}
