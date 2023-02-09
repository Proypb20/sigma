import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { NovedadComponent } from './list/novedad.component';
import { NovedadDetailComponent } from './detail/novedad-detail.component';
import { NovedadUpdateComponent } from './update/novedad-update.component';
import { NovedadDeleteDialogComponent } from './delete/novedad-delete-dialog.component';
import { NovedadRoutingModule } from './route/novedad-routing.module';

@NgModule({
  imports: [SharedModule, NovedadRoutingModule],
  declarations: [NovedadComponent, NovedadDetailComponent, NovedadUpdateComponent, NovedadDeleteDialogComponent],
})
export class NovedadModule {}
