import { ChangeDetectionStrategy, Component } from "@angular/core";
import { BehaviorSubject, ReplaySubject, Subject, switchMap, take } from "rxjs";
import { GameService } from "./game.service";
import { GameState } from "./model/game-state";
import { HouseIdentifier } from "./model/house-identifier";

@Component({
    selector: "app-root",
    templateUrl: "./app.component.html",
    styleUrls: ["./app.component.scss"],
    changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AppComponent {
    title = "mancala-ui";
    gameOver$: Subject<boolean> = new BehaviorSubject<boolean>(true);
    gameState$: Subject<GameState> = new ReplaySubject<GameState>();

    constructor(private readonly gameService: GameService) {}

    startNewGame() {
        this.gameService
            .newGame()
            .subscribe((state) => this.gameState$.next(state));
    }

    onHouseClicked(house: HouseIdentifier) {
        this.gameState$
            .pipe(
                take(1),
                switchMap((state) =>
                    this.gameService.play(state.activePlayer, house)
                )
            )
            .subscribe((state) => this.gameState$.next(state));
    }
}
