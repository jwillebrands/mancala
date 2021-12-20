package dev.willebrands.mancala.game;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class KalahaGameTest {
    private KalahaGame game;

    @BeforeEach
    public void setupGame() {
        game = KalahaGame.startNewGame();
    }

    @Test
    public void cannotSowOtherPlayersHouse() {
        assertFalse(game.sow(0, house("B1")).getValidationResult().isLegal());
    }

    @Test
    @DisplayName("Sow A3 from initial state; expect another turn")
    public void endingOnStoreGrantsAnotherTurn() {
        ensureLegalMoves(move(0, house("A3")));
        assertEquals(0, game.currentState().getActivePlayer());
        assertTrue(game.currentState().isActivePlayer(0));
    }

    @Test
    @DisplayName("Sow A3 from initial state; expect score 1")
    public void endingOnStoreScoresAPoint() {
        ensureLegalMoves(move(0, house("A3")));
        assertEquals(1, game.currentState().getScore(0));
    }

    @Test
    @DisplayName("Sow 6 seeds from A2 in a 2 player, 2 house layout. Expect score 2-0")
    public void ownStoreIsIncludedForEachLapWhenSowing() {
        game = new KalahaGame(2, 6);
        ensureLegalMoves(move(0, house("A2")));
        assertEquals(2, game.currentState().getScore(0));
        assertEquals(0, game.currentState().getScore(1));
        assertEquals(7, game.currentState().getSeedCount(new HouseIdentifier(0, 0)));
        assertEquals(7, game.currentState().getSeedCount(new HouseIdentifier(1, 0)));
        assertEquals(7, game.currentState().getSeedCount(new HouseIdentifier(1, 1)));
        assertEquals(1, game.currentState().getSeedCount(new HouseIdentifier(0, 1)));
    }

    @Test
    public void sowingAHouseClearsItsSeeds() {
        HouseIdentifier house = house("A1");
        ensureLegalMoves(move(0, house));
        assertEquals(0, game.currentState().getSeedCount(house));
    }

    @Test
    public void sowingDistributesSeedsToAdjecentHoues() {
        ensureLegalMoves(move(0, house("A2")));
        assertEquals(0, game.currentState().getSeedCount(house("A2")));
        asList("A3", "A4", "A5", "A6")
                .forEach(id -> assertEquals(5, game.currentState().getSeedCount(house(id))));
    }

    private void ensureLegalMoves(Move... moves) {
        for (Move move : moves) {
            MoveValidationResult moveResult =
                    game.sow(move.player, move.house).getValidationResult();
            assertTrue(
                    moveResult.isLegal(),
                    () ->
                            String.format(
                                    "Invalid move '%s': %s",
                                    move, moveResult.getMessage().orElse("")));
        }
    }

    static Move move(int player, HouseIdentifier house) {
        return new Move(player, house);
    }

    static HouseIdentifier house(String moveNotation) {
        int player = moveNotation.charAt(0) - 'A';
        int houseIndex = Integer.parseInt(moveNotation.substring(1)) - 1;
        return new HouseIdentifier(player, houseIndex);
    }

    @Data
    static class Move {
        final int player;
        final HouseIdentifier house;
    }
}
