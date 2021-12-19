package dev.willebrands.mancala.infra;

import dev.willebrands.mancala.game.KalahaGame;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class InmemoryKalahaGameRepository implements KalahaGameRepository {
    private final Map<String, KalahaGame> games = new HashMap<>();

    @Override
    public Optional<KalahaGame> findById(String id) {
        return Optional.ofNullable(games.get(id));
    }

    @Override
    public String createNewGame() {
        String id = UUID.randomUUID().toString();
        games.put(id, KalahaGame.startNewGame());
        return id;
    }
}
