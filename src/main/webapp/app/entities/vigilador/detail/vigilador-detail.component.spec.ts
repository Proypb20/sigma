import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { VigiladorDetailComponent } from './vigilador-detail.component';

describe('Vigilador Management Detail Component', () => {
  let comp: VigiladorDetailComponent;
  let fixture: ComponentFixture<VigiladorDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [VigiladorDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ vigilador: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(VigiladorDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(VigiladorDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load vigilador on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.vigilador).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
