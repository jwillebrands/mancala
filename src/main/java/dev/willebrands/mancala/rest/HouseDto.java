package dev.willebrands.mancala.rest;

import dev.willebrands.mancala.game.HouseIdentifier;
import lombok.Data;

@Data
class HouseDto {
    private final HouseIdentifier identifier;
    private final int seedCount;
}
