import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ServicioFormService, ServicioFormGroup } from './servicio-form.service';
import { IServicio } from '../servicio.model';
import { ServicioService } from '../service/servicio.service';
import { IVigilador } from 'app/entities/vigilador/vigilador.model';
import { VigiladorService } from 'app/entities/vigilador/service/vigilador.service';
import { IObjetivo } from 'app/entities/objetivo/objetivo.model';
import { ObjetivoService } from 'app/entities/objetivo/service/objetivo.service';

@Component({
  selector: 'jhi-servicio-update',
  templateUrl: './servicio-update.component.html',
})
export class ServicioUpdateComponent implements OnInit {
  isSaving = false;
  servicio: IServicio | null = null;

  vigiladorsSharedCollection: IVigilador[] = [];
  objetivosSharedCollection: IObjetivo[] = [];

  editForm: ServicioFormGroup = this.servicioFormService.createServicioFormGroup();

  constructor(
    protected servicioService: ServicioService,
    protected servicioFormService: ServicioFormService,
    protected vigiladorService: VigiladorService,
    protected objetivoService: ObjetivoService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareVigilador = (o1: IVigilador | null, o2: IVigilador | null): boolean => this.vigiladorService.compareVigilador(o1, o2);

  compareObjetivo = (o1: IObjetivo | null, o2: IObjetivo | null): boolean => this.objetivoService.compareObjetivo(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ servicio }) => {
      this.servicio = servicio;
      if (servicio) {
        this.updateForm(servicio);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const servicio = this.servicioFormService.getServicio(this.editForm);
    if (servicio.id !== null) {
      this.subscribeToSaveResponse(this.servicioService.update(servicio));
    } else {
      this.subscribeToSaveResponse(this.servicioService.create(servicio));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IServicio>>): void {
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

  protected updateForm(servicio: IServicio): void {
    this.servicio = servicio;
    this.servicioFormService.resetForm(this.editForm, servicio);

    this.vigiladorsSharedCollection = this.vigiladorService.addVigiladorToCollectionIfMissing<IVigilador>(
      this.vigiladorsSharedCollection,
      servicio.vigilador
    );
    this.objetivosSharedCollection = this.objetivoService.addObjetivoToCollectionIfMissing<IObjetivo>(
      this.objetivosSharedCollection,
      servicio.objetivo
    );
  }

  protected loadRelationshipsOptions(): void {
    this.vigiladorService
      .query()
      .pipe(map((res: HttpResponse<IVigilador[]>) => res.body ?? []))
      .pipe(
        map((vigiladors: IVigilador[]) =>
          this.vigiladorService.addVigiladorToCollectionIfMissing<IVigilador>(vigiladors, this.servicio?.vigilador)
        )
      )
      .subscribe((vigiladors: IVigilador[]) => (this.vigiladorsSharedCollection = vigiladors));

    this.objetivoService
      .query()
      .pipe(map((res: HttpResponse<IObjetivo[]>) => res.body ?? []))
      .pipe(
        map((objetivos: IObjetivo[]) =>
          this.objetivoService.addObjetivoToCollectionIfMissing<IObjetivo>(objetivos, this.servicio?.objetivo)
        )
      )
      .subscribe((objetivos: IObjetivo[]) => (this.objetivosSharedCollection = objetivos));
  }
}
