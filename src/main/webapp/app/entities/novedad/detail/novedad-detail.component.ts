import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { INovedad } from '../novedad.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-novedad-detail',
  templateUrl: './novedad-detail.component.html',
})
export class NovedadDetailComponent implements OnInit {
  novedad: INovedad | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ novedad }) => {
      this.novedad = novedad;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
