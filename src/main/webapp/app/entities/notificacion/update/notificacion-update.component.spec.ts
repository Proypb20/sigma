import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { NotificacionFormService } from './notificacion-form.service';
import { NotificacionService } from '../service/notificacion.service';
import { INotificacion } from '../notificacion.model';
import { INovedad } from 'app/entities/novedad/novedad.model';
import { NovedadService } from 'app/entities/novedad/service/novedad.service';
import { IVigilador } from 'app/entities/vigilador/vigilador.model';
import { VigiladorService } from 'app/entities/vigilador/service/vigilador.service';

import { NotificacionUpdateComponent } from './notificacion-update.component';

describe('Notificacion Management Update Component', () => {
  let comp: NotificacionUpdateComponent;
  let fixture: ComponentFixture<NotificacionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let notificacionFormService: NotificacionFormService;
  let notificacionService: NotificacionService;
  let novedadService: NovedadService;
  let vigiladorService: VigiladorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [NotificacionUpdateComponent],
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
      .overrideTemplate(NotificacionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(NotificacionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    notificacionFormService = TestBed.inject(NotificacionFormService);
    notificacionService = TestBed.inject(NotificacionService);
    novedadService = TestBed.inject(NovedadService);
    vigiladorService = TestBed.inject(VigiladorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Novedad query and add missing value', () => {
      const notificacion: INotificacion = { id: 456 };
      const novedad: INovedad = { id: 23312 };
      notificacion.novedad = novedad;

      const novedadCollection: INovedad[] = [{ id: 94313 }];
      jest.spyOn(novedadService, 'query').mockReturnValue(of(new HttpResponse({ body: novedadCollection })));
      const additionalNovedads = [novedad];
      const expectedCollection: INovedad[] = [...additionalNovedads, ...novedadCollection];
      jest.spyOn(novedadService, 'addNovedadToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ notificacion });
      comp.ngOnInit();

      expect(novedadService.query).toHaveBeenCalled();
      expect(novedadService.addNovedadToCollectionIfMissing).toHaveBeenCalledWith(
        novedadCollection,
        ...additionalNovedads.map(expect.objectContaining)
      );
      expect(comp.novedadsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Vigilador query and add missing value', () => {
      const notificacion: INotificacion = { id: 456 };
      const vigilador: IVigilador = { id: 27394 };
      notificacion.vigilador = vigilador;

      const vigiladorCollection: IVigilador[] = [{ id: 33106 }];
      jest.spyOn(vigiladorService, 'query').mockReturnValue(of(new HttpResponse({ body: vigiladorCollection })));
      const additionalVigiladors = [vigilador];
      const expectedCollection: IVigilador[] = [...additionalVigiladors, ...vigiladorCollection];
      jest.spyOn(vigiladorService, 'addVigiladorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ notificacion });
      comp.ngOnInit();

      expect(vigiladorService.query).toHaveBeenCalled();
      expect(vigiladorService.addVigiladorToCollectionIfMissing).toHaveBeenCalledWith(
        vigiladorCollection,
        ...additionalVigiladors.map(expect.objectContaining)
      );
      expect(comp.vigiladorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const notificacion: INotificacion = { id: 456 };
      const novedad: INovedad = { id: 74880 };
      notificacion.novedad = novedad;
      const vigilador: IVigilador = { id: 31049 };
      notificacion.vigilador = vigilador;

      activatedRoute.data = of({ notificacion });
      comp.ngOnInit();

      expect(comp.novedadsSharedCollection).toContain(novedad);
      expect(comp.vigiladorsSharedCollection).toContain(vigilador);
      expect(comp.notificacion).toEqual(notificacion);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INotificacion>>();
      const notificacion = { id: 123 };
      jest.spyOn(notificacionFormService, 'getNotificacion').mockReturnValue(notificacion);
      jest.spyOn(notificacionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ notificacion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: notificacion }));
      saveSubject.complete();

      // THEN
      expect(notificacionFormService.getNotificacion).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(notificacionService.update).toHaveBeenCalledWith(expect.objectContaining(notificacion));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INotificacion>>();
      const notificacion = { id: 123 };
      jest.spyOn(notificacionFormService, 'getNotificacion').mockReturnValue({ id: null });
      jest.spyOn(notificacionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ notificacion: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: notificacion }));
      saveSubject.complete();

      // THEN
      expect(notificacionFormService.getNotificacion).toHaveBeenCalled();
      expect(notificacionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INotificacion>>();
      const notificacion = { id: 123 };
      jest.spyOn(notificacionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ notificacion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(notificacionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareNovedad', () => {
      it('Should forward to novedadService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(novedadService, 'compareNovedad');
        comp.compareNovedad(entity, entity2);
        expect(novedadService.compareNovedad).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareVigilador', () => {
      it('Should forward to vigiladorService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(vigiladorService, 'compareVigilador');
        comp.compareVigilador(entity, entity2);
        expect(vigiladorService.compareVigilador).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
