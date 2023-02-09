import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { INovedad } from '../novedad.model';
import { NovedadService } from '../service/novedad.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './novedad-delete-dialog.component.html',
})
export class NovedadDeleteDialogComponent {
  novedad?: INovedad;

  constructor(protected novedadService: NovedadService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.novedadService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
