import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { INotificacion } from '../notificacion.model';
import { NotificacionService } from '../service/notificacion.service';

@Component({
  selector: 'jhi-notificacion-detail',
  templateUrl: './notificacion-detail.component.html',
})
export class NotificacionDetailComponent implements OnInit {
  notificacion: INotificacion | null = null;

  constructor(protected activatedRoute: ActivatedRoute, protected notificacionService: NotificacionService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ notificacion }) => {
      this.notificacion = notificacion;
    });
  }

  previousState(): void {
    this.subscribeToSaveResponse(this.notificacionService.read(this.notificacion!));
    window.history.back();
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
    // Api for lalala
  }
}
