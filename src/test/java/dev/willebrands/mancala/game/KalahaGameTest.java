package dev.willebrands.mancala.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.Function;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class KalahaGameTest {
    private KalahaGame game;

    @BeforeEach
    public void setupGame() {
        game = KalahaGame.startNewGame();
    }

    private int activePlayer() {
        return game.currentState().getActivePlayer();
    }

    private int inactivePlayer() {
        return (activePlayer() + 1) % game.currentState().numPlayers();
    }

    @Test
    public void cannotSowOtherPlayersHouse() {
        assertFalse(
                game.sow(activePlayer(), new HouseIdentifier(inactivePlayer(), 0))
                        .getValidationResult()
                        .isLegal());
    }

    @Test
    @DisplayName("Sow A2 from initial state; expect another turn")
    public void endingOnStoreGrantsAnotherTurn() {
        int playerOne = activePlayer();
        ensureLegalMoves(game -> game.sow(playerOne, new HouseIdentifier(playerOne, 2)));
        assertEquals(playerOne, activePlayer());
    }

    @Test
    @DisplayName("Sow A2 from initial state; expect score 1")
    public void endingOnStoreScoresAPoint() {
        int playerOne = activePlayer();
        ensureLegalMoves(game -> game.sow(playerOne, new HouseIdentifier(playerOne, 2)));
        assertEquals(1, game.currentState().getScore(playerOne));
    }

    @Test
    @DisplayName("Sow 6 seeds from A1 in a 2 player, 2 house layout. Expect score 2-0")
    public void ownStoreIsIncludedForEachLapWhenSowing() {
        game = new KalahaGame(2, 6);
        int playerOne = activePlayer();
        ensureLegalMoves(game -> game.sow(playerOne, new HouseIdentifier(playerOne, 1)));
        assertEquals(2, game.currentState().getScore(playerOne));
        assertEquals(0, game.currentState().getScore(playerOne + 1));
        assertEquals(7, game.currentState().getSeedCount(new HouseIdentifier(0, 0)));
        assertEquals(7, game.currentState().getSeedCount(new HouseIdentifier(1, 0)));
        assertEquals(7, game.currentState().getSeedCount(new HouseIdentifier(1, 1)));
        assertEquals(1, game.currentState().getSeedCount(new HouseIdentifier(0, 1)));
    }

    @Test
    public void sowingAHouseClearsItsSeeds() {
        HouseIdentifier house = new HouseIdentifier(activePlayer(), 1);
        ensureLegalMoves(game -> game.sow(activePlayer(), house));
        assertEquals(0, game.currentState().getSeedCount(house));
    }

    @Test
    public void sowingDistributesSeedsToAdjecentHoues() {
        int player = activePlayer();
        HouseIdentifier house = new HouseIdentifier(player, 1);
        ensureLegalMoves(game -> game.sow(player, house));
        assertEquals(0, game.currentState().getSeedCount(house));
        for (int i = 2; i < 6; i++) {
            assertEquals(5, game.currentState().getSeedCount(new HouseIdentifier(player, i)));
        }
    }

    @SafeVarargs
    private void ensureLegalMoves(Function<KalahaGame, MoveResult>... moves) {
        for (Function<KalahaGame, MoveResult> move : moves) {
            MoveValidationResult moveResult = move.apply(game).getValidationResult();
            assertTrue(moveResult.isLegal(), () -> moveResult.getMessage().orElse(""));
        }
    }
}
