import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IObjetivo } from '../objetivo.model';
import { ObjetivoService } from '../service/objetivo.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './objetivo-delete-dialog.component.html',
})
export class ObjetivoDeleteDialogComponent {
  objetivo?: IObjetivo;

  constructor(protected objetivoService: ObjetivoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.objetivoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
