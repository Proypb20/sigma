import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVigilador } from '../vigilador.model';
import { VigiladorService } from '../service/vigilador.service';

@Injectable({ providedIn: 'root' })
export class VigiladorRoutingResolveService implements Resolve<IVigilador | null> {
  constructor(protected service: VigiladorService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVigilador | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((vigilador: HttpResponse<IVigilador>) => {
          if (vigilador.body) {
            return of(vigilador.body);
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
