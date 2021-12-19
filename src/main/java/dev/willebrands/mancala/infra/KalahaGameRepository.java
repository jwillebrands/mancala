package dev.willebrands.mancala.infra;

import dev.willebrands.mancala.game.KalahaGame;
import java.util.Optional;

public interface KalahaGameRepository {
    Optional<KalahaGame> findById(String id);

    String createNewGame();
}
