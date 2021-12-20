package dev.willebrands.mancala.rest;

import dev.willebrands.mancala.game.MoveResult;
import lombok.Data;

@Data
public class MoveResultDto {
    final boolean valid;
    final String invalidMessage;
    final GameDto newState;

    static MoveResultDto from(MoveResult result, GameDto game) {
        return new MoveResultDto(
                result.getValidationResult().isLegal(),
                result.getValidationResult().getMessage().orElse(null),
                game);
    }
}
