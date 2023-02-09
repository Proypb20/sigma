import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VigiladorComponent } from '../list/vigilador.component';
import { VigiladorDetailComponent } from '../detail/vigilador-detail.component';
import { VigiladorUpdateComponent } from '../update/vigilador-update.component';
import { VigiladorRoutingResolveService } from './vigilador-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const vigiladorRoute: Routes = [
  {
    path: '',
    component: VigiladorComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VigiladorDetailComponent,
    resolve: {
      vigilador: VigiladorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VigiladorUpdateComponent,
    resolve: {
      vigilador: VigiladorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VigiladorUpdateComponent,
    resolve: {
      vigilador: VigiladorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(vigiladorRoute)],
  exports: [RouterModule],
})
export class VigiladorRoutingModule {}
