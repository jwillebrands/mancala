import { ComponentFixture, TestBed } from "@angular/core/testing";

import { MancalaStoreComponent } from "./mancala-store.component";

describe("MancalaStoreComponent", () => {
    let component: MancalaStoreComponent;
    let fixture: ComponentFixture<MancalaStoreComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [MancalaStoreComponent],
        }).compileComponents();
    });

    beforeEach(() => {
        fixture = TestBed.createComponent(MancalaStoreComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it("should create", () => {
        expect(component).toBeTruthy();
    });
});
