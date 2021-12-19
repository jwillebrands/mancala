package dev.willebrands.mancala.game;

import static org.junit.jupiter.api.Assertions.*;

import java.util.function.Function;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class KalahaGameTest {
    private KalahaGame defaultGame;

    @BeforeEach
    public void setupGame() {
        defaultGame = KalahaGame.startNewGame();
    }

    private int activePlayer() {
        return defaultGame.currentState().getActivePlayer();
    }

    private int inactivePlayer() {
        return (activePlayer() + 1) % defaultGame.currentState().numPlayers();
    }

    @Test
    public void cannotSowOtherPlayersHouse() {
        assertFalse(
                defaultGame
                        .sow(activePlayer(), new HouseIdentifier(inactivePlayer(), 0))
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
        assertEquals(1, defaultGame.currentState().getScore(playerOne));
    }

    @Test
    public void sowingAHouseClearsItsSeeds() {
        HouseIdentifier house = new HouseIdentifier(activePlayer(), 1);
        ensureLegalMoves(game -> game.sow(activePlayer(), house));
        assertEquals(0, defaultGame.currentState().getSeedCount(house));
    }

    @Test
    public void sowingDistributesSeedsToAdjecentHoues() {
        int player = activePlayer();
        HouseIdentifier house = new HouseIdentifier(player, 1);
        ensureLegalMoves(game -> game.sow(player, house));
        assertEquals(0, defaultGame.currentState().getSeedCount(house));
        for (int i = 2; i < 6; i++) {
            assertEquals(
                    5, defaultGame.currentState().getSeedCount(new HouseIdentifier(player, i)));
        }
    }

    @SafeVarargs
    private void ensureLegalMoves(Function<KalahaGame, MoveResult>... moves) {
        for (Function<KalahaGame, MoveResult> move : moves) {
            MoveValidationResult moveResult = move.apply(defaultGame).getValidationResult();
            assertTrue(moveResult.isLegal(), () -> moveResult.getMessage().orElse(""));
        }
    }
}
