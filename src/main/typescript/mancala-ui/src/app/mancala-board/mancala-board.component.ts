import {
    ChangeDetectionStrategy,
    Component,
    EventEmitter,
    Input,
    Output,
} from "@angular/core";
import { GameState } from "../model/game-state";
import { HouseIdentifier } from "../model/house-identifier";

@Component({
    selector: "app-mancala-board",
    templateUrl: "./mancala-board.component.html",
    styleUrls: ["./mancala-board.component.scss"],
    changeDetection: ChangeDetectionStrategy.OnPush,
})
export class MancalaBoardComponent {
    @Input()
    gameState!: GameState;

    @Output()
    houseClicked: EventEmitter<HouseIdentifier> = new EventEmitter<HouseIdentifier>();

    belongsToActivePlayer(ownerId: number) {
        return this.gameState.activePlayer === ownerId;
    }

    onHouseClicked(house: HouseIdentifier) {
        if (this.belongsToActivePlayer(house.player)) {
            this.houseClicked.emit(house);
        }
    }
}
