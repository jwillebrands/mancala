package dev.willebrands.mancala.game;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MoveValidationResult {
    private final boolean isLegal;
    private final String message;

    public Optional<String> getMessage() {
        return Optional.ofNullable(message);
    }

    static MoveValidationResult invalid(String message) {
        return new MoveValidationResult(false, requireNonNull(message));
    }

    static MoveValidationResult valid() {
        return new MoveValidationResult(true, null);
    }
}
