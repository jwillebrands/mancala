import { Observable, of } from "rxjs";
import { GameService } from "./game.service";
import { GameState } from "./model/game-state";
import { HouseIdentifier } from "./model/house-identifier";

export class GameStubService implements GameService {
    private game: GameState = this.createDefaultGame();

    createDefaultGame(): GameState {
        return {
            activePlayer: 0,
            housesPerPlayer: 6,
            numPlayers: 2,
            stores: [
                { identifier: { index: 6, playerId: 0 }, seeds: 0 },
                { identifier: { index: 6, playerId: 1 }, seeds: 0 },
            ],
            houseLayout: [
                [...Array(6).keys()].map((id) => ({
                    identifier: { index: id, playerId: 0 },
                    seeds: 4,
                })),
                [...Array(6).keys()].map((id) => ({
                    identifier: { index: id, playerId: 1 },
                    seeds: 4,
                })),
            ],
        };
    }

    newGame(): Observable<GameState> {
        this.game = this.createDefaultGame();
        return this.queryState("dummy");
    }

    queryState(id: string): Observable<GameState> {
        return of(this.game);
    }

    play(activePlayer: number, house: HouseIdentifier): Observable<GameState> {
        this.game.houseLayout[house.playerId][house.index] = {
            identifier: house,
            seeds: 0,
        };
        this.game = Object.assign(this.game, {
            activePlayer: this.game.activePlayer === 1 ? 0 : 1,
        });
        return this.queryState("dummy");
    }
}
