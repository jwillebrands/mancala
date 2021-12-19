import { ComponentFixture, TestBed } from "@angular/core/testing";

import { MancalaHouseComponent } from "./mancala-house.component";

describe("MancalaHouseComponent", () => {
    let component: MancalaHouseComponent;
    let fixture: ComponentFixture<MancalaHouseComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [MancalaHouseComponent],
        }).compileComponents();
    });

    beforeEach(() => {
        fixture = TestBed.createComponent(MancalaHouseComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it("should create", () => {
        expect(component).toBeTruthy();
    });
});
