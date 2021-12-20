package dev.willebrands.mancala.game;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.summingInt;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import org.springframework.data.util.Pair;

public class KalahaGame {
    static final int DEFAULT_HOUSE_COUNT = 6;
    static final int DEFAULT_SEED_COUNT = 4;
    static final int NUM_PLAYERS = 2;
    private final MutableGameState state;

    public KalahaGame(int houseCount, int seedCount) {
        state = new MutableGameState(NUM_PLAYERS, houseCount, houseId -> seedCount);
    }

    public GameState currentState() {
        return state;
    }

    private Predicate<HouseIdentifier> isNotOpponentsStore(int executingPlayer) {
        return isStore().and(house -> !house.isOwnHouse(executingPlayer)).negate();
    }

    private Predicate<HouseIdentifier> isStore() {
        return houseIdentifier -> houseIdentifier.getIndex() == state.playerHouseCount();
    }

    public MoveResult sow(int executingPlayer, HouseIdentifier houseIdentifier) {
        if (!state.isActivePlayer(executingPlayer)) {
            return MoveResult.illegalMove(
                    String.format("Player %d is the active player", state.getActivePlayer()));
        }
        if (!state.isActivePlayer(houseIdentifier.getPlayer())) {
            return MoveResult.illegalMove("Cannot sow from another players' house.");
        }
        if (state.getSeedCount(houseIdentifier) == 0) {
            return MoveResult.illegalMove("Cannot sow from an empty house.");
        }
        HouseIdentifier lastHouseSown = doSow(getSowingIterator(executingPlayer, houseIdentifier));
        handleEndOfSowing(executingPlayer, lastHouseSown);
        return MoveResult.legalMove();
    }

    private void handleEndOfSowing(int executingPlayer, HouseIdentifier lastHouseSown) {
        if (isStore().test(lastHouseSown)) {
            return; // Player gets another turn, no other actions;
        }
        if (lastHouseSown.isOwnHouse(executingPlayer) && state.getSeedCount(lastHouseSown) == 1) {
            state.capture(lastHouseSown, executingPlayer);
            state.capture(oppositeHouse(lastHouseSown), executingPlayer);
        }
        checkEndGameCondition();
        endTurn();
    }

    private void checkEndGameCondition() {
        Map<Integer, Integer> seedCountByPlayer =
                state.houses().excludeStores().stream()
                        .limit((long) state.numPlayers() * state.playerHouseCount())
                        .map(house -> Pair.of(house, state.getSeedCount(house)))
                        .collect(
                                groupingBy(
                                        pair -> pair.getFirst().getPlayer(),
                                        mapping(Pair::getFirst, summingInt(state::getSeedCount))));
        if (seedCountByPlayer.containsValue(0)) {
            // Each player captures its own remaining houses
            state.houses().excludeStores().stream()
                    .limit((long) state.numPlayers() * state.playerHouseCount())
                    .forEach(house -> state.capture(house, house.getPlayer()));
        }
        state.setWinner(state.getScore(0) > state.getScore(1) ? 0 : 1);
    }

    private void endTurn() {
        state.changeActivePlayer((state.getActivePlayer() + 1) % state.numPlayers());
    }

    private Iterator<HouseIdentifier> getSowingIterator(int player, HouseIdentifier pitToReap) {
        return state.houses().includeStores(true).counterClockwise().startAfter(pitToReap).stream()
                .filter(isNotOpponentsStore(player))
                .limit(state.removeSeedsFromHouse(pitToReap))
                .iterator();
    }

    private HouseIdentifier doSow(Iterator<HouseIdentifier> housesToSow) {
        HouseIdentifier house = null;
        while (housesToSow.hasNext()) {
            house = housesToSow.next();
            state.sowSeedsInHouse(house, 1);
        }
        return house;
    }

    private HouseIdentifier oppositeHouse(HouseIdentifier house) {
        return new HouseIdentifier(
                (house.getPlayer() + 1) % state.numPlayers(),
                state.playerHouseCount() - house.getIndex() - 1);
    }

    public List<List<HouseIdentifier>> boardLayout() {
        Map<Integer, List<HouseIdentifier>> housesByPlayer =
                state
                        .houses()
                        .excludeStores()
                        .counterClockwise()
                        .startWith(new HouseIdentifier(0, 0))
                        .stream()
                        .limit((long) state.numPlayers() * state.playerHouseCount())
                        .collect(groupingBy(HouseIdentifier::getPlayer));
        Collections.reverse(housesByPlayer.get(1));
        return asList(housesByPlayer.get(0), housesByPlayer.get(1));
    }

    public static KalahaGame startNewGame() {
        return new KalahaGame(DEFAULT_HOUSE_COUNT, DEFAULT_SEED_COUNT);
    }
}
