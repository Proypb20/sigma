import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IObjetivo } from '../objetivo.model';
import { ObjetivoService } from '../service/objetivo.service';

@Injectable({ providedIn: 'root' })
export class ObjetivoRoutingResolveService implements Resolve<IObjetivo | null> {
  constructor(protected service: ObjetivoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IObjetivo | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((objetivo: HttpResponse<IObjetivo>) => {
          if (objetivo.body) {
            return of(objetivo.body);
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
