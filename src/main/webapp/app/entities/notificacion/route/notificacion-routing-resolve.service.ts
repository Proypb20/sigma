import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { INotificacion } from '../notificacion.model';
import { NotificacionService } from '../service/notificacion.service';

@Injectable({ providedIn: 'root' })
export class NotificacionRoutingResolveService implements Resolve<INotificacion | null> {
  constructor(protected service: NotificacionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<INotificacion | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((notificacion: HttpResponse<INotificacion>) => {
          if (notificacion.body) {
            return of(notificacion.body);
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
