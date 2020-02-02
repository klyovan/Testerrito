import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateTestComponent } from './updateTest.component';

describe('TestComponent', () => {
  let component: UpdateTestComponent;
  let fixture: ComponentFixture<UpdateTestComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UpdateTestComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UpdateTestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
