import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { VigiladorFormService, VigiladorFormGroup } from './vigilador-form.service';
import { IVigilador } from '../vigilador.model';
import { VigiladorService } from '../service/vigilador.service';
import { ICategoria } from 'app/entities/categoria/categoria.model';
import { CategoriaService } from 'app/entities/categoria/service/categoria.service';
import { IObjetivo } from 'app/entities/objetivo/objetivo.model';
import { ObjetivoService } from 'app/entities/objetivo/service/objetivo.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-vigilador-update',
  templateUrl: './vigilador-update.component.html',
})
export class VigiladorUpdateComponent implements OnInit {
  isSaving = false;
  vigilador: IVigilador | null = null;

  categoriasSharedCollection: ICategoria[] = [];
  objetivosSharedCollection: IObjetivo[] = [];
  usersSharedCollection: IUser[] = [];

  editForm: VigiladorFormGroup = this.vigiladorFormService.createVigiladorFormGroup();

  constructor(
    protected vigiladorService: VigiladorService,
    protected vigiladorFormService: VigiladorFormService,
    protected categoriaService: CategoriaService,
    protected objetivoService: ObjetivoService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareCategoria = (o1: ICategoria | null, o2: ICategoria | null): boolean => this.categoriaService.compareCategoria(o1, o2);

  compareObjetivo = (o1: IObjetivo | null, o2: IObjetivo | null): boolean => this.objetivoService.compareObjetivo(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vigilador }) => {
      this.vigilador = vigilador;
      if (vigilador) {
        this.updateForm(vigilador);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vigilador = this.vigiladorFormService.getVigilador(this.editForm);
    if (vigilador.id !== null) {
      this.subscribeToSaveResponse(this.vigiladorService.update(vigilador));
    } else {
      this.subscribeToSaveResponse(this.vigiladorService.create(vigilador));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVigilador>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(vigilador: IVigilador): void {
    this.vigilador = vigilador;
    this.vigiladorFormService.resetForm(this.editForm, vigilador);

    this.categoriasSharedCollection = this.categoriaService.addCategoriaToCollectionIfMissing<ICategoria>(
      this.categoriasSharedCollection,
      vigilador.categoria
    );
    this.objetivosSharedCollection = this.objetivoService.addObjetivoToCollectionIfMissing<IObjetivo>(
      this.objetivosSharedCollection,
      vigilador.objetivo
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, vigilador.user);
  }

  protected loadRelationshipsOptions(): void {
    this.categoriaService
      .query()
      .pipe(map((res: HttpResponse<ICategoria[]>) => res.body ?? []))
      .pipe(
        map((categorias: ICategoria[]) =>
          this.categoriaService.addCategoriaToCollectionIfMissing<ICategoria>(categorias, this.vigilador?.categoria)
        )
      )
      .subscribe((categorias: ICategoria[]) => (this.categoriasSharedCollection = categorias));

    this.objetivoService
      .query()
      .pipe(map((res: HttpResponse<IObjetivo[]>) => res.body ?? []))
      .pipe(
        map((objetivos: IObjetivo[]) =>
          this.objetivoService.addObjetivoToCollectionIfMissing<IObjetivo>(objetivos, this.vigilador?.objetivo)
        )
      )
      .subscribe((objetivos: IObjetivo[]) => (this.objetivosSharedCollection = objetivos));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.vigilador?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
