package dev.willebrands.mancala.game;

import static java.util.Objects.requireNonNull;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class HouseStream {
    private final int playerCount;
    private final int housesPerPlayer;

    private HouseStream(int playerCount, int housesPerPlayer) {
        this.playerCount = playerCount;
        this.housesPerPlayer = housesPerPlayer;
    }

    private HouseIdentifier absoluteHouseIndexToIdentifier(int index) {
        int playerId = index / housesPerPlayer % playerCount;
        int houseId = index % housesPerPlayer;
        return new HouseIdentifier(playerId, houseId);
    }

    private int getAbsoluteIndex(HouseIdentifier startExclusive) {
        return startExclusive.getPlayer() * housesPerPlayer + startExclusive.getIndex();
    }

    private Stream<HouseIdentifier> stream(HouseIdentifier start, int direction, int startOffset) {
        return IntStream.iterate(
                        getAbsoluteIndex(requireNonNull(start)) + startOffset, i -> (i + direction))
                .mapToObj(this::absoluteHouseIndexToIdentifier);
    }

    static HouseStreamBuilder houseStream(int playerCount, int housesPerPlayer) {
        return new HouseStreamBuilder(playerCount, housesPerPlayer);
    }

    public static class HouseStreamBuilder {
        private final int playerCount;
        private final int housesPerPlayer;
        private int direction = 1;
        private boolean includeStores = false;
        private int offset = 0;
        private HouseIdentifier start = new HouseIdentifier(0, 0);

        HouseStreamBuilder(int playerCount, int housesPerPlayer) {
            this.playerCount = playerCount;
            this.housesPerPlayer = housesPerPlayer;
        }

        HouseStreamBuilder clockwise() {
            direction = -1;
            return this;
        }

        HouseStreamBuilder counterClockwise() {
            direction = 1;
            return this;
        }

        HouseStreamBuilder includeStores(boolean includeStores) {
            this.includeStores = includeStores;
            return this;
        }

        HouseStreamBuilder excludeStores() {
            return includeStores(false);
        }

        HouseStreamBuilder includeStores() {
            return includeStores(true);
        }

        HouseStreamBuilder startAfter(HouseIdentifier identifier) {
            start = identifier;
            offset = 1;
            return this;
        }

        HouseStreamBuilder startWith(HouseIdentifier identifier) {
            start = identifier;
            offset = 0;
            return this;
        }

        Stream<HouseIdentifier> stream() {
            int housesPerPlayer = this.housesPerPlayer;
            if (includeStores) {
                housesPerPlayer += 1;
            }
            return new HouseStream(playerCount, housesPerPlayer)
                    .stream(requireNonNull(start), direction, offset * direction);
        }
    }
}
