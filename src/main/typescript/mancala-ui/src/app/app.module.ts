import { NgModule } from "@angular/core";
import { MatButtonModule } from "@angular/material/button";
import { BrowserModule } from "@angular/platform-browser";

import { AppComponent } from "./app.component";
import { MancalaBoardModule } from "./mancala-board/mancala-board.module";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";

@NgModule({
    declarations: [AppComponent],
    imports: [
        BrowserModule,
        MancalaBoardModule,
        MatButtonModule,
        BrowserAnimationsModule,
    ],
    providers: [],
    bootstrap: [AppComponent],
})
export class AppModule {}
