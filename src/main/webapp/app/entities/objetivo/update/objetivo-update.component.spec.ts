import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ObjetivoFormService } from './objetivo-form.service';
import { ObjetivoService } from '../service/objetivo.service';
import { IObjetivo } from '../objetivo.model';

import { ObjetivoUpdateComponent } from './objetivo-update.component';

describe('Objetivo Management Update Component', () => {
  let comp: ObjetivoUpdateComponent;
  let fixture: ComponentFixture<ObjetivoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let objetivoFormService: ObjetivoFormService;
  let objetivoService: ObjetivoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ObjetivoUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ObjetivoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ObjetivoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    objetivoFormService = TestBed.inject(ObjetivoFormService);
    objetivoService = TestBed.inject(ObjetivoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const objetivo: IObjetivo = { id: 456 };

      activatedRoute.data = of({ objetivo });
      comp.ngOnInit();

      expect(comp.objetivo).toEqual(objetivo);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IObjetivo>>();
      const objetivo = { id: 123 };
      jest.spyOn(objetivoFormService, 'getObjetivo').mockReturnValue(objetivo);
      jest.spyOn(objetivoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ objetivo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: objetivo }));
      saveSubject.complete();

      // THEN
      expect(objetivoFormService.getObjetivo).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(objetivoService.update).toHaveBeenCalledWith(expect.objectContaining(objetivo));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IObjetivo>>();
      const objetivo = { id: 123 };
      jest.spyOn(objetivoFormService, 'getObjetivo').mockReturnValue({ id: null });
      jest.spyOn(objetivoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ objetivo: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: objetivo }));
      saveSubject.complete();

      // THEN
      expect(objetivoFormService.getObjetivo).toHaveBeenCalled();
      expect(objetivoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IObjetivo>>();
      const objetivo = { id: 123 };
      jest.spyOn(objetivoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ objetivo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(objetivoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
