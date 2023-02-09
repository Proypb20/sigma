import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { VigiladorFormService } from './vigilador-form.service';
import { VigiladorService } from '../service/vigilador.service';
import { IVigilador } from '../vigilador.model';
import { ICategoria } from 'app/entities/categoria/categoria.model';
import { CategoriaService } from 'app/entities/categoria/service/categoria.service';
import { IObjetivo } from 'app/entities/objetivo/objetivo.model';
import { ObjetivoService } from 'app/entities/objetivo/service/objetivo.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { VigiladorUpdateComponent } from './vigilador-update.component';

describe('Vigilador Management Update Component', () => {
  let comp: VigiladorUpdateComponent;
  let fixture: ComponentFixture<VigiladorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let vigiladorFormService: VigiladorFormService;
  let vigiladorService: VigiladorService;
  let categoriaService: CategoriaService;
  let objetivoService: ObjetivoService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [VigiladorUpdateComponent],
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
      .overrideTemplate(VigiladorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VigiladorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    vigiladorFormService = TestBed.inject(VigiladorFormService);
    vigiladorService = TestBed.inject(VigiladorService);
    categoriaService = TestBed.inject(CategoriaService);
    objetivoService = TestBed.inject(ObjetivoService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Categoria query and add missing value', () => {
      const vigilador: IVigilador = { id: 456 };
      const categoria: ICategoria = { id: 86213 };
      vigilador.categoria = categoria;

      const categoriaCollection: ICategoria[] = [{ id: 74044 }];
      jest.spyOn(categoriaService, 'query').mockReturnValue(of(new HttpResponse({ body: categoriaCollection })));
      const additionalCategorias = [categoria];
      const expectedCollection: ICategoria[] = [...additionalCategorias, ...categoriaCollection];
      jest.spyOn(categoriaService, 'addCategoriaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ vigilador });
      comp.ngOnInit();

      expect(categoriaService.query).toHaveBeenCalled();
      expect(categoriaService.addCategoriaToCollectionIfMissing).toHaveBeenCalledWith(
        categoriaCollection,
        ...additionalCategorias.map(expect.objectContaining)
      );
      expect(comp.categoriasSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Objetivo query and add missing value', () => {
      const vigilador: IVigilador = { id: 456 };
      const objetivo: IObjetivo = { id: 63195 };
      vigilador.objetivo = objetivo;

      const objetivoCollection: IObjetivo[] = [{ id: 20936 }];
      jest.spyOn(objetivoService, 'query').mockReturnValue(of(new HttpResponse({ body: objetivoCollection })));
      const additionalObjetivos = [objetivo];
      const expectedCollection: IObjetivo[] = [...additionalObjetivos, ...objetivoCollection];
      jest.spyOn(objetivoService, 'addObjetivoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ vigilador });
      comp.ngOnInit();

      expect(objetivoService.query).toHaveBeenCalled();
      expect(objetivoService.addObjetivoToCollectionIfMissing).toHaveBeenCalledWith(
        objetivoCollection,
        ...additionalObjetivos.map(expect.objectContaining)
      );
      expect(comp.objetivosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const vigilador: IVigilador = { id: 456 };
      const user: IUser = { id: 1393 };
      vigilador.user = user;

      const userCollection: IUser[] = [{ id: 96981 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ vigilador });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const vigilador: IVigilador = { id: 456 };
      const categoria: ICategoria = { id: 45878 };
      vigilador.categoria = categoria;
      const objetivo: IObjetivo = { id: 61936 };
      vigilador.objetivo = objetivo;
      const user: IUser = { id: 50455 };
      vigilador.user = user;

      activatedRoute.data = of({ vigilador });
      comp.ngOnInit();

      expect(comp.categoriasSharedCollection).toContain(categoria);
      expect(comp.objetivosSharedCollection).toContain(objetivo);
      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.vigilador).toEqual(vigilador);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVigilador>>();
      const vigilador = { id: 123 };
      jest.spyOn(vigiladorFormService, 'getVigilador').mockReturnValue(vigilador);
      jest.spyOn(vigiladorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vigilador });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vigilador }));
      saveSubject.complete();

      // THEN
      expect(vigiladorFormService.getVigilador).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(vigiladorService.update).toHaveBeenCalledWith(expect.objectContaining(vigilador));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVigilador>>();
      const vigilador = { id: 123 };
      jest.spyOn(vigiladorFormService, 'getVigilador').mockReturnValue({ id: null });
      jest.spyOn(vigiladorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vigilador: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vigilador }));
      saveSubject.complete();

      // THEN
      expect(vigiladorFormService.getVigilador).toHaveBeenCalled();
      expect(vigiladorService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVigilador>>();
      const vigilador = { id: 123 };
      jest.spyOn(vigiladorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vigilador });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(vigiladorService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCategoria', () => {
      it('Should forward to categoriaService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(categoriaService, 'compareCategoria');
        comp.compareCategoria(entity, entity2);
        expect(categoriaService.compareCategoria).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
