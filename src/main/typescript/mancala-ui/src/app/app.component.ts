import { ChangeDetectionStrategy, Component } from "@angular/core";
import { MatSnackBar } from "@angular/material/snack-bar";
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
    gameState$: Subject<GameState> = new ReplaySubject<GameState>(1);

    constructor(
        private readonly gameService: GameService,
        private snackBar: MatSnackBar
    ) {}

    startNewGame() {
        this.gameService.newGame().subscribe({
            next: (state) => this.gameState$.next(state),
            error: (error) => this.snackBar.open(error.message),
        });
    }

    onHouseClicked(house: HouseIdentifier) {
        console.log(house);
        this.gameState$
            .pipe(
                take(1),
                switchMap((state) => this.gameService.play(state.id, house))
            )
            .subscribe({
                next: (state) => this.gameState$.next(state),
                error: (error) => this.snackBar.open(error.message),
            });
    }
}
