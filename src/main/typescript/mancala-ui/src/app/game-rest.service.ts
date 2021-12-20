import { HttpClient, HttpResponse } from "@angular/common/http";
import { Observable, of, switchMap, throwError } from "rxjs";
import { environment } from "../environments/environment";
import { GameService } from "./game.service";
import { GameState } from "./model/game-state";
import { HouseIdentifier } from "./model/house-identifier";

interface MoveResult {
    valid: boolean;
    invalidMessage?: string;
    newState: GameState;
}

export class GameRestService implements GameService {
    private readonly baseUrl = `${environment.backendUrl}/api/v1`;

    constructor(private httpClient: HttpClient) {}

    newGame(): Observable<GameState> {
        return this.httpClient
            .post(`${this.baseUrl}/games`, {}, { observe: "response" })
            .pipe(
                switchMap((response: HttpResponse<any>) =>
                    response.status === 201
                        ? this.httpClient.get<GameState>(
                              response.headers.get("Location")!
                          )
                        : throwError(() => new Error(response.statusText))
                )
            );
    }

    play(gameId: string, house: HouseIdentifier): Observable<GameState> {
        console.log(house);
        return this.httpClient
            .post<MoveResult>(`${this.baseUrl}/games/${gameId}/moves`, {
                playerId: house.player,
                houseId: house.index,
            })
            .pipe(
                switchMap((result) =>
                    result.valid
                        ? of(result.newState)
                        : throwError(() => new Error(result.invalidMessage))
                )
            );
    }

    queryState(id: string): Observable<GameState> {
        return this.httpClient.get<GameState>(`${this.baseUrl}/games/${id}`);
    }
}
