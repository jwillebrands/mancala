package dev.willebrands.mancala.rest;

import static java.util.stream.Collectors.toList;

import dev.willebrands.mancala.game.GameState;
import dev.willebrands.mancala.game.HouseIdentifier;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import lombok.Data;

@Data
public class GameDto {
    final int activePlayer;
    final Map<HouseIdentifier, Integer> board;
    final Integer winningPlayer;
    final List<Integer> playerScores;

    static GameDto from(GameState state) {
        return new GameDto(
                state.getActivePlayer(),
                Collections.emptyMap(),
                null,
                IntStream.range(0, state.numPlayers()).mapToObj(state::getScore).collect(toList()));
    }
}
