package dev.willebrands.mancala.game;

import static dev.willebrands.mancala.game.HouseStream.houseStream;
import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class MutableGameState implements GameState {
    private final int numPlayers;
    private final int numHousesPerPlayer;

    private final Map<HouseIdentifier, Integer> seedCount = new HashMap<>();
    private int activePlayer = 0;

    public MutableGameState(
            int numPlayers,
            int numHousesPerPlayer,
            Function<HouseIdentifier, Integer> initialValueProvider) {
        this.numPlayers = numPlayers;
        this.numHousesPerPlayer = numHousesPerPlayer;
        for (int playerId = 0; playerId < numPlayers; playerId++) {
            for (int houseId = 0; houseId < numHousesPerPlayer; houseId++) {
                HouseIdentifier houseIdentifier = new HouseIdentifier(playerId, houseId);
                seedCount.put(houseIdentifier, initialValueProvider.apply(houseIdentifier));
            }
            seedCount.put(new HouseIdentifier(playerId, numHousesPerPlayer), 0);
        }
    }

    @Override
    public int numPlayers() {
        return numPlayers;
    }

    @Override
    public int getActivePlayer() {
        return activePlayer;
    }

    @Override
    public int playerHouseCount() {
        return numHousesPerPlayer;
    }

    @Override
    public int getScore(int playerId) {
        checkValidPlayerId(playerId);
        return seedCount.get(new HouseIdentifier(playerId, numHousesPerPlayer));
    }

    @Override
    public int getSeedCount(HouseIdentifier houseIdentifier) {
        checkValidHouse(houseIdentifier, false);
        return seedCount.get(houseIdentifier);
    }

    @Override
    public HouseStream.HouseStreamBuilder houses() {
        return houseStream(numPlayers, numHousesPerPlayer);
    }

    /**
     * Removes all seeds from the house identified by {@code houseIdentifier}
     *
     * @param houseIdentifier
     * @return The amount the seeds that were contained in the house
     */
    Integer removeSeedsFromHouse(HouseIdentifier houseIdentifier) {
        checkValidHouse(houseIdentifier, false);
        return seedCount.put(houseIdentifier, 0);
    }

    /**
     * Sows {@code numSeeds} seeds into the house identified by {@code houseIdentifier}
     *
     * @param houseIdentifier
     * @param numSeeds
     * @return Amount of seeds in the house as a result of the sowing action
     */
    Integer sowSeedsInHouse(HouseIdentifier houseIdentifier, int numSeeds) {
        checkValidHouse(houseIdentifier, true);
        return seedCount.merge(houseIdentifier, numSeeds, Integer::sum);
    }

    /**
     * Capture all seeds from the house identified by {@code houseIdentifier} and add them to the
     * capturing players score.
     *
     * @param houseIdentifier
     * @param capturingPlayer
     */
    void capture(HouseIdentifier houseIdentifier, int capturingPlayer) {
        checkValidHouse(houseIdentifier, false);
        checkValidPlayerId(capturingPlayer);
        score(capturingPlayer, removeSeedsFromHouse(houseIdentifier));
    }

    /**
     * Adds the amount of {@code seeds} to the players' score
     *
     * @param player
     * @param seeds
     */
    void score(int player, int seeds) {
        checkValidPlayerId(player);
        sowSeedsInHouse(new HouseIdentifier(player, numHousesPerPlayer), seeds);
    }

    void changeActivePlayer(int nextPlayer) {
        checkValidPlayerId(nextPlayer);
        activePlayer = nextPlayer;
    }

    private void checkValidHouse(HouseIdentifier houseIdentifier, boolean allowStore) {
        if (!seedCount.containsKey(requireNonNull(houseIdentifier))) {
            throw new IllegalArgumentException("No such house: " + houseIdentifier);
        }
        if (!allowStore && houseIdentifier.getIndex() == numHousesPerPlayer) {
            throw new IllegalArgumentException("Stores are not allowed for this action.");
        }
    }

    private void checkValidPlayerId(int playerId) {
        if (playerId < 0 || playerId >= numPlayers()) {
            throw new IllegalArgumentException("Invalid player id: " + playerId);
        }
    }
}
