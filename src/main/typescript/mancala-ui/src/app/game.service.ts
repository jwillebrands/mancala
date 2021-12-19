import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { GameStubService } from "./game-stub.service";
import { GameState } from "./model/game-state";
import { HouseIdentifier } from "./model/house-identifier";

@Injectable({
    providedIn: "root",
    useClass: GameStubService,
    deps: [],
})
export abstract class GameService {
    abstract newGame(): Observable<GameState>;

    abstract queryState(id: string): Observable<GameState>;

    abstract play(gameId: string, house: HouseIdentifier): Observable<GameState>;
}
