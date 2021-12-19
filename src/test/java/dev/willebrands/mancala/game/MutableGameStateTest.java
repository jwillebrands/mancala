package dev.willebrands.mancala.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MutableGameStateTest {
    @Test
    public void removingSeedsChangesTheHouseToZero() {
        MutableGameState gameState = new MutableGameState(2, 6, id -> 4);
        HouseIdentifier house = new HouseIdentifier(0, 0);
        assertEquals(4, gameState.removeSeedsFromHouse(house));
        assertEquals(0, gameState.getSeedCount(house));
    }

    @Test
    public void sowingSeedsToAHouseIncrementsThatHouseByTheSuppliedAmount() {
        MutableGameState gameState = new MutableGameState(2, 6, id -> 4);
        HouseIdentifier house = new HouseIdentifier(0, 0);
        assertEquals(6, gameState.sowSeedsInHouse(house, 2));
        assertEquals(6, gameState.getSeedCount(house));
    }

    @Test
    @DisplayName("Capturing a house clears it and adds its contents to player's score")
    public void captureSeedsScoreTest() {
        MutableGameState state = new MutableGameState(2, 6, id -> 4);
        HouseIdentifier house = new HouseIdentifier(0, 2);
        state.capture(house, 0);
        assertEquals(4, state.getScore(0));
        assertEquals(0, state.getScore(1));
        assertEquals(0, state.getSeedCount(house));
    }

    @Test
    public void scoreIncrementsScoreBySuppliedAmount() {
        MutableGameState state = new MutableGameState(2, 6, id -> 4);
        state.score(0, 1);
        assertEquals(1, state.getScore(0));
        assertEquals(0, state.getScore(1));
    }

    @Test
    public void canChangeActivePlayer() {
        MutableGameState state = new MutableGameState(2, 6, id -> 4);
        assertEquals(0, state.getActivePlayer(), "Play starts with player 0");
        state.changeActivePlayer(1);
        assertEquals(1, state.getActivePlayer());
        state.changeActivePlayer(1);
        assertEquals(
                1,
                state.getActivePlayer(),
                "Changing active player to current value is a valid NOOP.");
    }

    @Test
    public void testIsActivePlayer() {
        MutableGameState state = new MutableGameState(2, 6, id -> 4);
        assertTrue(state.isActivePlayer(0));
        state.changeActivePlayer(1);
        assertFalse(state.isActivePlayer(0));
    }
}
