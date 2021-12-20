import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { GameRestService } from "./game-rest.service";
import { GameState } from "./model/game-state";
import { HouseIdentifier } from "./model/house-identifier";

@Injectable({
    providedIn: "root",
    useClass: GameRestService,
    deps: [HttpClient],
})
export abstract class GameService {
    abstract newGame(): Observable<GameState>;

    abstract queryState(id: string): Observable<GameState>;

    abstract play(
        gameId: string,
        house: HouseIdentifier
    ): Observable<GameState>;
}
