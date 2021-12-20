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

    @Test
    public void landingOnAnyNonStoreEndsTurn() {
        ensureLegalMoves(move(0, house("A1")));
        assertEquals(1, game.currentState().getActivePlayer());
    }

    @Test
    public void endingSowOnOwnEmptyHouseCapturesItAndOppositeHouse() {
        ensureLegalMoves(
                // Play A5 (4 seeds) Score 1
                move(0, house("A5")),
                // Opponent plays B1 (5 seeds)
                move(1, house("B1")),
                // Play A1 (4) ending on empty A5, capture A5 (1 seed) + B2 (6 seeds)
                move(0, house("A1")));
        assertEquals(
                0,
                game.currentState().getSeedCount(house("A5")),
                "Sowing last seed in empty cup should capture it.");
        assertEquals(
                0,
                game.currentState().getSeedCount(house("B2")),
                "Sowing last seed in empty cup should capture opposite");
        assertEquals(
                8,
                game.currentState().getScore(0),
                "Expect 1 point from first turn, 6 from capturing B2 and 1 from capturing A5");
    }

    @Test
    public void endingSowingOnOpponentEmptyHouseCapturesNothing() {
        ensureLegalMoves(
                // Play A1 (4 seeds)
                move(0, house("A1")),
                // Opponent plays B4 (4 seeds); scores 1
                move(1, house("B4")));
        assertEquals(
                1,
                game.currentState().getSeedCount(house("A1")),
                "Ending sowing on empty opponent cell should not capture seed.");

        assertEquals(5, game.currentState().getSeedCount(house("B6")));
    }

    @Test
    public void whenAllCellsOfAPlayerAreClearedTheGameEnds() {
        game = new KalahaGame(2, 2);
        ensureLegalMoves(move(0, house("A1")), move(0, house("A2")));
        assertTrue(game.currentState().winningPlayer().isPresent());
    }

    @Test
    public void testPlayerTwoOppositeCapture() {
        ensureLegalMoves(
                // Play A2 (4 seeds)
                move(0, house("A2")),
                // Opponent plays B5 (4 seeds); scores 1
                move(1, house("B5")),
                move(0, house("A1")),
                // Opponent plays B1, landing on empty B5; capturing it (1) and A2 (2); scores 3
                move(1, house("B1")));
        assertEquals(
                0,
                game.currentState().getSeedCount(house("B5")),
                "Sowing last seed in empty cup should capture it.");
        assertEquals(
                0,
                game.currentState().getSeedCount(house("A2")),
                "Sowing last seed in empty cup should capture opposite");
        assertEquals(4, game.currentState().getScore(1));
    }

    @Test
    public void testEndGameScoring() {
        game = new KalahaGame(2, 2);
        ensureLegalMoves(
                // Play A1, sow in A2 (3) and Store (1)
                move(0, house("A1")),
                // Play A2, sow in Store (2), B1(3), B2(3)
                move(0, house("A2")));
        assertEquals(
                2, game.currentState().getScore(0), "Expected 2 points for player 1 from sowing");
        assertEquals(
                6,
                game.currentState().getScore(1),
                "Expected 6 points for player 2 from capturing leftover houses after opponent"
                        + " clearing his.");
        assertTrue(game.currentState().winningPlayer().isPresent());
        assertEquals(1, game.currentState().winningPlayer().get());
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
