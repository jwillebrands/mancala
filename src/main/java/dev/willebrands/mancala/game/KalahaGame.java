package dev.willebrands.mancala.game;

public class KalahaGame {
    static final int DEFAULT_HOUSE_COUNT = 6;
    static final int DEFAULT_SEED_COUNT = 4;
    static final int NUM_PLAYERS = 2;

    public KalahaGame(int houseCount, int seedCount) {}

    GameState currentState() {
        return null;
    }

    public MoveResult sow(int executingPlayer, HouseIdentifier houseIdentifier) {
        return MoveResult.legalMove();
    }

    public static KalahaGame startNewGame() {
        return new KalahaGame(DEFAULT_HOUSE_COUNT, DEFAULT_SEED_COUNT);
    }
}
