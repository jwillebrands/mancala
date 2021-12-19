package dev.willebrands.mancala.game;

import lombok.Data;

@Data
public class HouseIdentifier {
    private final int player;
    private final int index;

    boolean isOwnHouse(int player) {
        return this.player == player;
    }
}
