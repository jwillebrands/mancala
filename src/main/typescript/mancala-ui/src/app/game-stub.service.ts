import { Observable, of } from "rxjs";
import { GameService } from "./game.service";
import { GameState } from "./model/game-state";
import { HouseIdentifier } from "./model/house-identifier";

export class GameStubService implements GameService {
    private game: GameState = this.createDefaultGame();

    createDefaultGame(): GameState {
        return {
            id: new Date().toString(),
            activePlayer: 0,
            playerScores: [0, 0],
            board: [
                [...Array(6).keys()].map((id) => ({
                    identifier: { index: id, player: 0 },
                    seedCount: 4,
                })),
                [...Array(6).keys()].map((id) => ({
                    identifier: { index: id, player: 1 },
                    seedCount: 4,
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

    play(gameId: string, house: HouseIdentifier): Observable<GameState> {
        this.game.board[house.player][house.index] = {
            identifier: house,
            seedCount: 0,
        };
        this.game = Object.assign(this.game, {
            activePlayer: this.game.activePlayer === 1 ? 0 : 1,
        });
        return this.queryState("dummy");
    }
}
