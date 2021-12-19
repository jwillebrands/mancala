import { HouseIdentifier } from "./house-identifier";

export interface CellState {
    readonly identifier: HouseIdentifier;
    readonly seeds: number;
}

export interface GameState {
    readonly numPlayers: number;
    readonly housesPerPlayer: number;
    readonly activePlayer: number;
    readonly houseLayout: CellState[][];
    readonly stores: CellState[];
    readonly winningPlayer?: number;
}
