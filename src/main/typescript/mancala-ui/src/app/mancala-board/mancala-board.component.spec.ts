import { ComponentFixture, TestBed } from "@angular/core/testing";

import { MancalaBoardComponent } from "./mancala-board.component";

describe("MancalaBoardComponent", () => {
    let component: MancalaBoardComponent;
    let fixture: ComponentFixture<MancalaBoardComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [MancalaBoardComponent],
        }).compileComponents();
    });

    beforeEach(() => {
        fixture = TestBed.createComponent(MancalaBoardComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it("should create", () => {
        expect(component).toBeTruthy();
    });
});
