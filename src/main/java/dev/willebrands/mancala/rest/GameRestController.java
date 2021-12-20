package dev.willebrands.mancala.rest;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

import dev.willebrands.mancala.game.HouseIdentifier;
import dev.willebrands.mancala.game.KalahaGame;
import dev.willebrands.mancala.game.MoveResult;
import dev.willebrands.mancala.infra.KalahaGameRepository;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

@RestController
@RequestMapping(path = "/api/v1/games")
public class GameRestController {
    private KalahaGameRepository repository;

    GameRestController(KalahaGameRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<Void> startNewGame(@RequestBody NewGameRequest request) {
        String gameId = repository.createNewGame();
        URI newGameLocation =
                MvcUriComponentsBuilder.fromMethodCall(on(this.getClass()).getGameState(gameId))
                        .build()
                        .toUri();
        return ResponseEntity.created(newGameLocation).build();
    }

    @GetMapping("/{id}")
    public GameDto getGameState(@PathVariable("id") String gameId) {
        KalahaGame game = repository.findById(gameId).orElseThrow(ResourceNotFoundException::new);
        return GameDto.from(game.currentState());
    }

    @PostMapping("/{id}/moves")
    public MoveResultDto playMove(@PathVariable("id") String gameId, @RequestBody MoveDto move) {
        KalahaGame game = repository.findById(gameId).orElseThrow(ResourceNotFoundException::new);
        MoveResult result =
                game.sow(move.playerId, new HouseIdentifier(move.playerId, move.houseId));
        return MoveResultDto.from(result, game.currentState());
    }
}
