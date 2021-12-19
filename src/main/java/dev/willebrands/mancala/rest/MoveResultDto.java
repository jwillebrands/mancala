package dev.willebrands.mancala.rest;

import dev.willebrands.mancala.game.GameState;
import dev.willebrands.mancala.game.MoveResult;
import lombok.Data;

@Data
public class MoveResultDto {
    final boolean valid;
    final String invalidMessage;
    final GameDto newState;

    static MoveResultDto from(MoveResult result, GameState currentState) {
        return new MoveResultDto(
                result.getValidationResult().isLegal(),
                result.getValidationResult().getMessage().orElse(null),
                GameDto.from(currentState));
    }
}
