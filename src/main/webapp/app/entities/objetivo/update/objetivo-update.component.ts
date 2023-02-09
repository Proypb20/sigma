import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ObjetivoFormService, ObjetivoFormGroup } from './objetivo-form.service';
import { IObjetivo } from '../objetivo.model';
import { ObjetivoService } from '../service/objetivo.service';

@Component({
  selector: 'jhi-objetivo-update',
  templateUrl: './objetivo-update.component.html',
})
export class ObjetivoUpdateComponent implements OnInit {
  isSaving = false;
  objetivo: IObjetivo | null = null;

  editForm: ObjetivoFormGroup = this.objetivoFormService.createObjetivoFormGroup();

  constructor(
    protected objetivoService: ObjetivoService,
    protected objetivoFormService: ObjetivoFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ objetivo }) => {
      this.objetivo = objetivo;
      if (objetivo) {
        this.updateForm(objetivo);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const objetivo = this.objetivoFormService.getObjetivo(this.editForm);
    if (objetivo.id !== null) {
      this.subscribeToSaveResponse(this.objetivoService.update(objetivo));
    } else {
      this.subscribeToSaveResponse(this.objetivoService.create(objetivo));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IObjetivo>>): void {
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

  protected updateForm(objetivo: IObjetivo): void {
    this.objetivo = objetivo;
    this.objetivoFormService.resetForm(this.editForm, objetivo);
  }
}
