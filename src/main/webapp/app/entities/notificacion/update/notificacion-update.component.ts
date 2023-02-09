import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { NotificacionFormService, NotificacionFormGroup } from './notificacion-form.service';
import { INotificacion } from '../notificacion.model';
import { NotificacionService } from '../service/notificacion.service';
import { INovedad } from 'app/entities/novedad/novedad.model';
import { NovedadService } from 'app/entities/novedad/service/novedad.service';
import { IVigilador } from 'app/entities/vigilador/vigilador.model';
import { VigiladorService } from 'app/entities/vigilador/service/vigilador.service';
import { Status } from 'app/entities/enumerations/status.model';

@Component({
  selector: 'jhi-notificacion-update',
  templateUrl: './notificacion-update.component.html',
})
export class NotificacionUpdateComponent implements OnInit {
  isSaving = false;
  notificacion: INotificacion | null = null;
  statusValues = Object.keys(Status);

  novedadsSharedCollection: INovedad[] = [];
  vigiladorsSharedCollection: IVigilador[] = [];

  editForm: NotificacionFormGroup = this.notificacionFormService.createNotificacionFormGroup();

  constructor(
    protected notificacionService: NotificacionService,
    protected notificacionFormService: NotificacionFormService,
    protected novedadService: NovedadService,
    protected vigiladorService: VigiladorService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareNovedad = (o1: INovedad | null, o2: INovedad | null): boolean => this.novedadService.compareNovedad(o1, o2);

  compareVigilador = (o1: IVigilador | null, o2: IVigilador | null): boolean => this.vigiladorService.compareVigilador(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ notificacion }) => {
      this.notificacion = notificacion;
      if (notificacion) {
        this.updateForm(notificacion);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const notificacion = this.notificacionFormService.getNotificacion(this.editForm);
    if (notificacion.id !== null) {
      this.subscribeToSaveResponse(this.notificacionService.update(notificacion));
    } else {
      this.subscribeToSaveResponse(this.notificacionService.create(notificacion));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INotificacion>>): void {
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

  protected updateForm(notificacion: INotificacion): void {
    this.notificacion = notificacion;
    this.notificacionFormService.resetForm(this.editForm, notificacion);

    this.novedadsSharedCollection = this.novedadService.addNovedadToCollectionIfMissing<INovedad>(
      this.novedadsSharedCollection,
      notificacion.novedad
    );
    this.vigiladorsSharedCollection = this.vigiladorService.addVigiladorToCollectionIfMissing<IVigilador>(
      this.vigiladorsSharedCollection,
      notificacion.vigilador
    );
  }

  protected loadRelationshipsOptions(): void {
    this.novedadService
      .query()
      .pipe(map((res: HttpResponse<INovedad[]>) => res.body ?? []))
      .pipe(
        map((novedads: INovedad[]) => this.novedadService.addNovedadToCollectionIfMissing<INovedad>(novedads, this.notificacion?.novedad))
      )
      .subscribe((novedads: INovedad[]) => (this.novedadsSharedCollection = novedads));

    this.vigiladorService
      .query()
      .pipe(map((res: HttpResponse<IVigilador[]>) => res.body ?? []))
      .pipe(
        map((vigiladors: IVigilador[]) =>
          this.vigiladorService.addVigiladorToCollectionIfMissing<IVigilador>(vigiladors, this.notificacion?.vigilador)
        )
      )
      .subscribe((vigiladors: IVigilador[]) => (this.vigiladorsSharedCollection = vigiladors));
  }
}
