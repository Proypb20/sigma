import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { INovedad } from '../novedad.model';
import { NovedadService } from '../service/novedad.service';

@Injectable({ providedIn: 'root' })
export class NovedadRoutingResolveService implements Resolve<INovedad | null> {
  constructor(protected service: NovedadService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<INovedad | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((novedad: HttpResponse<INovedad>) => {
          if (novedad.body) {
            return of(novedad.body);
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
