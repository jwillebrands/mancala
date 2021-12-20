import { HttpClientModule } from "@angular/common/http";
import { NgModule } from "@angular/core";
import { MatButtonModule } from "@angular/material/button";
import { MatSnackBarModule } from "@angular/material/snack-bar";
import { BrowserModule } from "@angular/platform-browser";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";

import { AppComponent } from "./app.component";
import { MancalaBoardModule } from "./mancala-board/mancala-board.module";

@NgModule({
    declarations: [AppComponent],
    imports: [
        BrowserModule,
        MancalaBoardModule,
        MatButtonModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatSnackBarModule,
    ],
    providers: [],
    bootstrap: [AppComponent],
})
export class AppModule {}
