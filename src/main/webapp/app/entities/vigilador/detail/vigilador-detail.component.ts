import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVigilador } from '../vigilador.model';

@Component({
  selector: 'jhi-vigilador-detail',
  templateUrl: './vigilador-detail.component.html',
})
export class VigiladorDetailComponent implements OnInit {
  vigilador: IVigilador | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vigilador }) => {
      this.vigilador = vigilador;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
