import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalRemarkComponent } from './modal-remark.component';

describe('ModalRemarkComponent', () => {
  let component: ModalRemarkComponent;
  let fixture: ComponentFixture<ModalRemarkComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ModalRemarkComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalRemarkComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
