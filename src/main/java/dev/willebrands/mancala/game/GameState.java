package dev.willebrands.mancala.game;

import dev.willebrands.mancala.game.HouseStream.HouseStreamBuilder;
import java.util.Optional;

public interface GameState {
    int numPlayers();

    int playerHouseCount();

    int getActivePlayer();

    int getScore(int playerId);

    int getSeedCount(HouseIdentifier houseIdentifier);

    HouseStreamBuilder houses();

    Optional<Integer> winningPlayer();

    default boolean isActivePlayer(int player) {
        return getActivePlayer() == player;
    }
}
