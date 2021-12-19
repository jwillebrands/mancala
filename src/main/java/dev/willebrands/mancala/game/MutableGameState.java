package dev.willebrands.mancala.game;

import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class MutableGameState implements GameState {
    private final int numPlayers;
    private final int numHousesPerPlayer;

    private final Map<HouseIdentifier, Integer> seedCount = new HashMap<>();
    private final int[] scoreByPlayerId;
    private int activePlayer = 0;

    public MutableGameState(
            int numPlayers,
            int numHousesPerPlayer,
            Function<HouseIdentifier, Integer> initialValueProvider) {
        this.numPlayers = numPlayers;
        this.numHousesPerPlayer = numHousesPerPlayer;
        scoreByPlayerId = new int[numPlayers];
        for (int houseId = 0; houseId < numHousesPerPlayer; houseId++) {
            for (int playerId = 0; playerId < numPlayers; playerId++) {
                HouseIdentifier houseIdentifier = new HouseIdentifier(playerId, houseId);
                seedCount.put(houseIdentifier, initialValueProvider.apply(houseIdentifier));
            }
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
        return scoreByPlayerId[playerId];
    }

    @Override
    public int getSeedCount(HouseIdentifier houseIdentifier) {
        checkValidHouse(houseIdentifier);
        return seedCount.get(houseIdentifier);
    }

    /**
     * Removes all seeds from the house identified by {@code houseIdentifier}
     *
     * @param houseIdentifier
     * @return The amount the seeds that were contained in the house
     */
    Integer removeSeedsFromHouse(HouseIdentifier houseIdentifier) {
        checkValidHouse(houseIdentifier);
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
        checkValidHouse(houseIdentifier);
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
        checkValidHouse(houseIdentifier);
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
        scoreByPlayerId[player] += seeds;
    }

    void changeActivePlayer(int nextPlayer) {
        checkValidPlayerId(nextPlayer);
        activePlayer = nextPlayer;
    }

    private void checkValidHouse(HouseIdentifier houseIdentifier) {
        if (!seedCount.containsKey(requireNonNull(houseIdentifier))) {
            throw new IllegalArgumentException("No such house: " + houseIdentifier);
        }
    }

    private void checkValidPlayerId(int playerId) {
        if (playerId < 0 || playerId >= numPlayers()) {
            throw new IllegalArgumentException("Invalid player id: " + playerId);
        }
    }
}
