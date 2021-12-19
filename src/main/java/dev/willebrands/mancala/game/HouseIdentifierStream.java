package dev.willebrands.mancala.game;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class HouseIdentifierStream {
    private final int playerCount;
    private final int housesPerPlayer;

    HouseIdentifierStream(int playerCount, int housesPerPlayer) {
        this.playerCount = playerCount;
        this.housesPerPlayer = housesPerPlayer;
    }

    private HouseIdentifier absoluteHouseIndexToIdentifier(int index) {
        if (index >= playerCount * housesPerPlayer) {
            throw new IndexOutOfBoundsException("Expected index in range of 0 to " + index);
        }
        int playerId = index / housesPerPlayer % playerCount;
        int houseId = index % housesPerPlayer;
        return new HouseIdentifier(playerId, houseId);
    }

    /**
     * Produces an infinite Stream of HouseIdentifiers starting after {@code startExclusive}
     *
     * @param startExclusive
     * @return
     */
    Stream<HouseIdentifier> counterClockwiseAfter(HouseIdentifier startExclusive) {
        return IntStream.iterate(
                        startExclusive.getPlayer() * housesPerPlayer + startExclusive.getIndex(),
                        i -> (i + 1) % (playerCount * housesPerPlayer))
                .mapToObj(this::absoluteHouseIndexToIdentifier);
    }
}
