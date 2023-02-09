import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ObjetivoComponent } from '../list/objetivo.component';
import { ObjetivoDetailComponent } from '../detail/objetivo-detail.component';
import { ObjetivoUpdateComponent } from '../update/objetivo-update.component';
import { ObjetivoRoutingResolveService } from './objetivo-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const objetivoRoute: Routes = [
  {
    path: '',
    component: ObjetivoComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ObjetivoDetailComponent,
    resolve: {
      objetivo: ObjetivoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ObjetivoUpdateComponent,
    resolve: {
      objetivo: ObjetivoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ObjetivoUpdateComponent,
    resolve: {
      objetivo: ObjetivoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(objetivoRoute)],
  exports: [RouterModule],
})
export class ObjetivoRoutingModule {}
