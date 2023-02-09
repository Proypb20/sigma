import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ObjetivoComponent } from './list/objetivo.component';
import { ObjetivoDetailComponent } from './detail/objetivo-detail.component';
import { ObjetivoUpdateComponent } from './update/objetivo-update.component';
import { ObjetivoDeleteDialogComponent } from './delete/objetivo-delete-dialog.component';
import { ObjetivoRoutingModule } from './route/objetivo-routing.module';

@NgModule({
  imports: [SharedModule, ObjetivoRoutingModule],
  declarations: [ObjetivoComponent, ObjetivoDetailComponent, ObjetivoUpdateComponent, ObjetivoDeleteDialogComponent],
})
export class ObjetivoModule {}
