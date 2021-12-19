package dev.willebrands.mancala.game;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import org.junit.jupiter.api.Test;

class HouseIdentifierStreamTest {

    @Test
    void counterClockwiseStartIsExclusive() {
        HouseIdentifierStream houseIdentifierStream = new HouseIdentifierStream(2, 6, false);
        HouseIdentifier start = new HouseIdentifier(0, 1);
        HouseIdentifier firstStreamElement =
                houseIdentifierStream.counterClockwiseAfter(start).findFirst().orElseThrow();
        assertEquals(new HouseIdentifier(0, 2), firstStreamElement);
    }

    @Test
    void counterClockwiseLoopsThroughAllInOrder() {
        HouseIdentifierStream houseIdentifierStream = new HouseIdentifierStream(2, 2, false);
        HouseIdentifier start = new HouseIdentifier(0, 0);
        assertIterableEquals(
                asList(
                        new HouseIdentifier(0, 1),
                        new HouseIdentifier(1, 0),
                        new HouseIdentifier(1, 1),
                        new HouseIdentifier(0, 0)),
                houseIdentifierStream.counterClockwiseAfter(start).limit(4).collect(toList()));
    }

    @Test
    void storesCanBeIncluded() {
        HouseIdentifierStream houseIdentifierStream = new HouseIdentifierStream(2, 2, true);
        HouseIdentifier start = new HouseIdentifier(0, 0);
        assertIterableEquals(
                asList(
                        new HouseIdentifier(0, 1),
                        new HouseIdentifier(0, 2),
                        new HouseIdentifier(1, 0),
                        new HouseIdentifier(1, 1)),
                houseIdentifierStream.counterClockwiseAfter(start).limit(4).collect(toList()));
    }

    @Test
    void houseAfterPlayersLastIsStore() {
        HouseIdentifierStream houseIdentifierStream = new HouseIdentifierStream(2, 2, true);
        HouseIdentifier start = new HouseIdentifier(0, 1);
        assertEquals(
                new HouseIdentifier(0, 2),
                houseIdentifierStream.counterClockwiseAfter(start).findFirst().orElseThrow());
    }
}
