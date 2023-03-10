import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { INotificacion } from '../notificacion.model';
import { NotificacionService } from '../service/notificacion.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './notificacion-delete-dialog.component.html',
})
export class NotificacionDeleteDialogComponent {
  notificacion?: INotificacion;

  constructor(protected notificacionService: NotificacionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.notificacionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
