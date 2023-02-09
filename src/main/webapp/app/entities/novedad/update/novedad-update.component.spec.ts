import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { NovedadFormService } from './novedad-form.service';
import { NovedadService } from '../service/novedad.service';
import { INovedad } from '../novedad.model';
import { IVigilador } from 'app/entities/vigilador/vigilador.model';
import { VigiladorService } from 'app/entities/vigilador/service/vigilador.service';
import { IObjetivo } from 'app/entities/objetivo/objetivo.model';
import { ObjetivoService } from 'app/entities/objetivo/service/objetivo.service';

import { NovedadUpdateComponent } from './novedad-update.component';

describe('Novedad Management Update Component', () => {
  let comp: NovedadUpdateComponent;
  let fixture: ComponentFixture<NovedadUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let novedadFormService: NovedadFormService;
  let novedadService: NovedadService;
  let vigiladorService: VigiladorService;
  let objetivoService: ObjetivoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [NovedadUpdateComponent],
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
      .overrideTemplate(NovedadUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(NovedadUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    novedadFormService = TestBed.inject(NovedadFormService);
    novedadService = TestBed.inject(NovedadService);
    vigiladorService = TestBed.inject(VigiladorService);
    objetivoService = TestBed.inject(ObjetivoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Vigilador query and add missing value', () => {
      const novedad: INovedad = { id: 456 };
      const vigilador: IVigilador = { id: 76820 };
      novedad.vigilador = vigilador;

      const vigiladorCollection: IVigilador[] = [{ id: 74737 }];
      jest.spyOn(vigiladorService, 'query').mockReturnValue(of(new HttpResponse({ body: vigiladorCollection })));
      const additionalVigiladors = [vigilador];
      const expectedCollection: IVigilador[] = [...additionalVigiladors, ...vigiladorCollection];
      jest.spyOn(vigiladorService, 'addVigiladorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ novedad });
      comp.ngOnInit();

      expect(vigiladorService.query).toHaveBeenCalled();
      expect(vigiladorService.addVigiladorToCollectionIfMissing).toHaveBeenCalledWith(
        vigiladorCollection,
        ...additionalVigiladors.map(expect.objectContaining)
      );
      expect(comp.vigiladorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Objetivo query and add missing value', () => {
      const novedad: INovedad = { id: 456 };
      const objetivo: IObjetivo = { id: 80482 };
      novedad.objetivo = objetivo;

      const objetivoCollection: IObjetivo[] = [{ id: 2913 }];
      jest.spyOn(objetivoService, 'query').mockReturnValue(of(new HttpResponse({ body: objetivoCollection })));
      const additionalObjetivos = [objetivo];
      const expectedCollection: IObjetivo[] = [...additionalObjetivos, ...objetivoCollection];
      jest.spyOn(objetivoService, 'addObjetivoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ novedad });
      comp.ngOnInit();

      expect(objetivoService.query).toHaveBeenCalled();
      expect(objetivoService.addObjetivoToCollectionIfMissing).toHaveBeenCalledWith(
        objetivoCollection,
        ...additionalObjetivos.map(expect.objectContaining)
      );
      expect(comp.objetivosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const novedad: INovedad = { id: 456 };
      const vigilador: IVigilador = { id: 6813 };
      novedad.vigilador = vigilador;
      const objetivo: IObjetivo = { id: 14705 };
      novedad.objetivo = objetivo;

      activatedRoute.data = of({ novedad });
      comp.ngOnInit();

      expect(comp.vigiladorsSharedCollection).toContain(vigilador);
      expect(comp.objetivosSharedCollection).toContain(objetivo);
      expect(comp.novedad).toEqual(novedad);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INovedad>>();
      const novedad = { id: 123 };
      jest.spyOn(novedadFormService, 'getNovedad').mockReturnValue(novedad);
      jest.spyOn(novedadService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ novedad });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: novedad }));
      saveSubject.complete();

      // THEN
      expect(novedadFormService.getNovedad).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(novedadService.update).toHaveBeenCalledWith(expect.objectContaining(novedad));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INovedad>>();
      const novedad = { id: 123 };
      jest.spyOn(novedadFormService, 'getNovedad').mockReturnValue({ id: null });
      jest.spyOn(novedadService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ novedad: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: novedad }));
      saveSubject.complete();

      // THEN
      expect(novedadFormService.getNovedad).toHaveBeenCalled();
      expect(novedadService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INovedad>>();
      const novedad = { id: 123 };
      jest.spyOn(novedadService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ novedad });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(novedadService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareVigilador', () => {
      it('Should forward to vigiladorService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(vigiladorService, 'compareVigilador');
        comp.compareVigilador(entity, entity2);
        expect(vigiladorService.compareVigilador).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareObjetivo', () => {
      it('Should forward to objetivoService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(objetivoService, 'compareObjetivo');
        comp.compareObjetivo(entity, entity2);
        expect(objetivoService.compareObjetivo).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
