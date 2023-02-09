import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { NovedadComponent } from '../list/novedad.component';
import { NovedadDetailComponent } from '../detail/novedad-detail.component';
import { NovedadUpdateComponent } from '../update/novedad-update.component';
import { NovedadRoutingResolveService } from './novedad-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const novedadRoute: Routes = [
  {
    path: '',
    component: NovedadComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: NovedadDetailComponent,
    resolve: {
      novedad: NovedadRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: NovedadUpdateComponent,
    resolve: {
      novedad: NovedadRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: NovedadUpdateComponent,
    resolve: {
      novedad: NovedadRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(novedadRoute)],
  exports: [RouterModule],
})
export class NovedadRoutingModule {}
