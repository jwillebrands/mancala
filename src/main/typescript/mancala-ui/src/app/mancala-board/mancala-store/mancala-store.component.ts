import { Component, Input } from "@angular/core";

@Component({
    selector: "app-mancala-store",
    templateUrl: "./mancala-store.component.html",
    styleUrls: ["./mancala-store.component.scss"],
})
export class MancalaStoreComponent {
    @Input()
    seedCount: number = 0;
}
