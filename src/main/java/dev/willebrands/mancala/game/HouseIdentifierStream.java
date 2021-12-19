package dev.willebrands.mancala.game;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class HouseIdentifierStream {
    private final int playerCount;
    private final int housesPerPlayer;

    HouseIdentifierStream(int playerCount, int housesPerPlayer, boolean includeStores) {
        this.playerCount = playerCount;
        if (includeStores) {
            housesPerPlayer++;
        }
        this.housesPerPlayer = housesPerPlayer;
    }

    private int maxIndex() {
        return playerCount * housesPerPlayer;
    }

    private HouseIdentifier absoluteHouseIndexToIdentifier(int index) {
        if (index >= maxIndex()) {
            throw new IndexOutOfBoundsException("Expected index in range of 0 to " + maxIndex());
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
                        startExclusive.getPlayer() * housesPerPlayer
                                + startExclusive.getIndex()
                                + 1,
                        i -> (i + 1) % maxIndex())
                .mapToObj(this::absoluteHouseIndexToIdentifier);
    }
}
