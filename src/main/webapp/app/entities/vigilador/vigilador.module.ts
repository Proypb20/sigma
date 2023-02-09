import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { VigiladorComponent } from './list/vigilador.component';
import { VigiladorDetailComponent } from './detail/vigilador-detail.component';
import { VigiladorUpdateComponent } from './update/vigilador-update.component';
import { VigiladorDeleteDialogComponent } from './delete/vigilador-delete-dialog.component';
import { VigiladorRoutingModule } from './route/vigilador-routing.module';

@NgModule({
  imports: [SharedModule, VigiladorRoutingModule],
  declarations: [VigiladorComponent, VigiladorDetailComponent, VigiladorUpdateComponent, VigiladorDeleteDialogComponent],
})
export class VigiladorModule {}
