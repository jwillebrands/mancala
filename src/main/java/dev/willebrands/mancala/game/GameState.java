package dev.willebrands.mancala.game;

public interface GameState {
    int numPlayers();

    int playerHouseCount();

    int getActivePlayer();

    int getScore(int playerId);

    int getSeedCount(HouseIdentifier houseIdentifier);

    default boolean isActivePlayer(int player) {
        return getActivePlayer() == player;
    }
}
