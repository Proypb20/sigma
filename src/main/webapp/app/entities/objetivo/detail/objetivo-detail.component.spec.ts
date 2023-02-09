import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ObjetivoDetailComponent } from './objetivo-detail.component';

describe('Objetivo Management Detail Component', () => {
  let comp: ObjetivoDetailComponent;
  let fixture: ComponentFixture<ObjetivoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ObjetivoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ objetivo: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ObjetivoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ObjetivoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load objetivo on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.objetivo).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
