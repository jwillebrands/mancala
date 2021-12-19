import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { MatButtonModule } from "@angular/material/button";
import { MancalaBoardComponent } from "./mancala-board.component";
import { MancalaHouseComponent } from "./mancala-house/mancala-house.component";
import { MancalaStoreComponent } from "./mancala-store/mancala-store.component";
import { TurnIndicatorComponent } from "./turn-indicator/turn-indicator.component";

@NgModule({
    declarations: [
        MancalaBoardComponent,
        MancalaStoreComponent,
        MancalaHouseComponent,
        TurnIndicatorComponent,
    ],
    exports: [MancalaBoardComponent],
    imports: [CommonModule, MatButtonModule],
})
export class MancalaBoardModule {}
