import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IServicio } from '../servicio.model';
import { ServicioService } from '../service/servicio.service';

@Injectable({ providedIn: 'root' })
export class ServicioRoutingResolveService implements Resolve<IServicio | null> {
  constructor(protected service: ServicioService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IServicio | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((servicio: HttpResponse<IServicio>) => {
          if (servicio.body) {
            return of(servicio.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
