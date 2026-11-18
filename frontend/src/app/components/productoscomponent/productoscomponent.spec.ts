import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Productoscomponent } from './productoscomponent';

describe('Productoscomponent', () => {
  let component: Productoscomponent;
  let fixture: ComponentFixture<Productoscomponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [Productoscomponent],
    }).compileComponents();

    fixture = TestBed.createComponent(Productoscomponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
