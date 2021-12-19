package dev.willebrands.mancala.game;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MoveResult {
    private final MoveValidationResult validationResult;

    static MoveResult illegalMove(String message) {
        return new MoveResult(MoveValidationResult.invalid(message));
    }

    static MoveResult legalMove() {
        return new MoveResult(MoveValidationResult.valid());
    }
}
