import { HouseIdentifier } from "./house-identifier";

export interface CellState {
    readonly identifier: HouseIdentifier;
    readonly seedCount: number;
}

export interface GameState {
    readonly id: string;
    readonly activePlayer: number;
    readonly board: CellState[][];
    readonly playerScores: number[];
    readonly winningPlayer?: number;
}
