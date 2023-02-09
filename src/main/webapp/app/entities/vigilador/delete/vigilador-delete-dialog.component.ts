import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IVigilador } from '../vigilador.model';
import { VigiladorService } from '../service/vigilador.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './vigilador-delete-dialog.component.html',
})
export class VigiladorDeleteDialogComponent {
  vigilador?: IVigilador;

  constructor(protected vigiladorService: VigiladorService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vigiladorService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
