package dev.willebrands.mancala.rest;

import static java.util.stream.Collectors.toList;

import dev.willebrands.mancala.game.GameState;
import dev.willebrands.mancala.game.HouseIdentifier;
import dev.willebrands.mancala.game.KalahaGame;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import lombok.Data;

@Data
public class GameDto {
    final String id;
    final int activePlayer;
    final List<List<HouseDto>> board;
    final Integer winningPlayer;
    final List<Integer> playerScores;

    private static List<List<HouseDto>> marshalBoard(
            List<List<HouseIdentifier>> layout,
            Function<HouseIdentifier, Integer> seedCountSupplier) {
        return layout.stream()
                .map(
                        row ->
                                row.stream()
                                        .map(id -> new HouseDto(id, seedCountSupplier.apply(id)))
                                        .collect(toList()))
                .collect(toList());
    }

    static GameDto from(String id, KalahaGame game) {
        GameState state = game.currentState();
        return new GameDto(
                id,
                state.getActivePlayer(),
                marshalBoard(game.boardLayout(), game.currentState()::getSeedCount),
                null,
                IntStream.range(0, state.numPlayers()).mapToObj(state::getScore).collect(toList()));
    }
}
