package dev.willebrands.mancala.game;

import java.util.function.Predicate;

public class KalahaGame {
    static final int DEFAULT_HOUSE_COUNT = 6;
    static final int DEFAULT_SEED_COUNT = 4;
    static final int NUM_PLAYERS = 2;
    private final MutableGameState state;

    public KalahaGame(int houseCount, int seedCount) {
        state = new MutableGameState(NUM_PLAYERS, houseCount, houseId -> seedCount);
    }

    GameState currentState() {
        return state;
    }

    private Predicate<HouseIdentifier> isNotOpponentsStore(int executingPlayer) {
        return houseIdentifier ->
                houseIdentifier.getPlayer() == executingPlayer
                        || houseIdentifier.getIndex() != state.playerHouseCount();
    }

    public MoveResult sow(int executingPlayer, HouseIdentifier houseIdentifier) {
        if (!state.isActivePlayer(executingPlayer)) {
            return MoveResult.illegalMove(
                    String.format("Player %d is the active player", state.getActivePlayer()));
        }
        if (!state.isActivePlayer(houseIdentifier.getPlayer())) {
            return MoveResult.illegalMove("Cannot sow from another players' house.");
        }
        if (state.getSeedCount(houseIdentifier) == 0) {
            return MoveResult.illegalMove("Cannot sow from an empty house.");
        }
        state.houses().includeStores(true).counterClockwise().startAfter(houseIdentifier).stream()
                .filter(isNotOpponentsStore(executingPlayer))
                .limit(state.removeSeedsFromHouse(houseIdentifier))
                .forEach(house -> state.sowSeedsInHouse(house, 1));
        return MoveResult.legalMove();
    }

    public static KalahaGame startNewGame() {
        return new KalahaGame(DEFAULT_HOUSE_COUNT, DEFAULT_SEED_COUNT);
    }
}
