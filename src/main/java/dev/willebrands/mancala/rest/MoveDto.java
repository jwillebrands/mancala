package dev.willebrands.mancala.rest;

import lombok.Data;

@Data
public class MoveDto {
    final int playerId;
    final int houseId;
}
