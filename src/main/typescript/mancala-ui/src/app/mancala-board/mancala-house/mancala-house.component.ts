import { Component, HostBinding, Input } from "@angular/core";

@Component({
    selector: "app-mancala-house",
    templateUrl: "./mancala-house.component.html",
    styleUrls: ["./mancala-house.component.scss"],
})
export class MancalaHouseComponent {
    @Input()
    @HostBinding("class.belongsToCurrentPlayer")
    belongsToCurrentPlayer!: boolean;

    @Input()
    seedCount: number = 0;
}
