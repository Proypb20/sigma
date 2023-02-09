import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { NovedadFormService, NovedadFormGroup } from './novedad-form.service';
import { INovedad } from '../novedad.model';
import { NovedadService } from '../service/novedad.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IVigilador } from 'app/entities/vigilador/vigilador.model';
import { VigiladorService } from 'app/entities/vigilador/service/vigilador.service';
import { IObjetivo } from 'app/entities/objetivo/objetivo.model';
import { ObjetivoService } from 'app/entities/objetivo/service/objetivo.service';
import { entregar } from 'app/entities/enumerations/entregar.model';

@Component({
  selector: 'jhi-novedad-update',
  templateUrl: './novedad-update.component.html',
})
export class NovedadUpdateComponent implements OnInit {
  isSaving = false;
  novedad: INovedad | null = null;
  entregarValues = Object.keys(entregar);

  vigiladorsSharedCollection: IVigilador[] = [];
  objetivosSharedCollection: IObjetivo[] = [];

  editForm: NovedadFormGroup = this.novedadFormService.createNovedadFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected novedadService: NovedadService,
    protected novedadFormService: NovedadFormService,
    protected vigiladorService: VigiladorService,
    protected objetivoService: ObjetivoService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareVigilador = (o1: IVigilador | null, o2: IVigilador | null): boolean => this.vigiladorService.compareVigilador(o1, o2);

  compareObjetivo = (o1: IObjetivo | null, o2: IObjetivo | null): boolean => this.objetivoService.compareObjetivo(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ novedad }) => {
      this.novedad = novedad;
      if (novedad) {
        this.updateForm(novedad);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('sigmaApp.error', { message: err.message })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const novedad = this.novedadFormService.getNovedad(this.editForm);
    if (novedad.id !== null) {
      this.subscribeToSaveResponse(this.novedadService.update(novedad));
    } else {
      this.subscribeToSaveResponse(this.novedadService.create(novedad));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INovedad>>): void {
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

  protected updateForm(novedad: INovedad): void {
    this.novedad = novedad;
    this.novedadFormService.resetForm(this.editForm, novedad);

    this.vigiladorsSharedCollection = this.vigiladorService.addVigiladorToCollectionIfMissing<IVigilador>(
      this.vigiladorsSharedCollection,
      novedad.vigilador
    );
    this.objetivosSharedCollection = this.objetivoService.addObjetivoToCollectionIfMissing<IObjetivo>(
      this.objetivosSharedCollection,
      novedad.objetivo
    );
  }

  protected loadRelationshipsOptions(): void {
    this.vigiladorService
      .query()
      .pipe(map((res: HttpResponse<IVigilador[]>) => res.body ?? []))
      .pipe(
        map((vigiladors: IVigilador[]) =>
          this.vigiladorService.addVigiladorToCollectionIfMissing<IVigilador>(vigiladors, this.novedad?.vigilador)
        )
      )
      .subscribe((vigiladors: IVigilador[]) => (this.vigiladorsSharedCollection = vigiladors));

    this.objetivoService
      .query()
      .pipe(map((res: HttpResponse<IObjetivo[]>) => res.body ?? []))
      .pipe(
        map((objetivos: IObjetivo[]) => this.objetivoService.addObjetivoToCollectionIfMissing<IObjetivo>(objetivos, this.novedad?.objetivo))
      )
      .subscribe((objetivos: IObjetivo[]) => (this.objetivosSharedCollection = objetivos));
  }
}
