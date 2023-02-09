import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { INotificacion, NewNotificacion } from '../notificacion.model';

export type PartialUpdateNotificacion = Partial<INotificacion> & Pick<INotificacion, 'id'>;

export type EntityResponseType = HttpResponse<INotificacion>;
export type EntityArrayResponseType = HttpResponse<INotificacion[]>;

@Injectable({ providedIn: 'root' })
export class NotificacionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/notificacions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(notificacion: NewNotificacion): Observable<EntityResponseType> {
    return this.http.post<INotificacion>(this.resourceUrl, notificacion, { observe: 'response' });
  }

  update(notificacion: INotificacion): Observable<EntityResponseType> {
    return this.http.put<INotificacion>(`${this.resourceUrl}/${this.getNotificacionIdentifier(notificacion)}`, notificacion, {
      observe: 'response',
    });
  }

  partialUpdate(notificacion: PartialUpdateNotificacion): Observable<EntityResponseType> {
    return this.http.patch<INotificacion>(`${this.resourceUrl}/${this.getNotificacionIdentifier(notificacion)}`, notificacion, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<INotificacion>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<INotificacion[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getNotificacionIdentifier(notificacion: Pick<INotificacion, 'id'>): number {
    return notificacion.id;
  }

  compareNotificacion(o1: Pick<INotificacion, 'id'> | null, o2: Pick<INotificacion, 'id'> | null): boolean {
    return o1 && o2 ? this.getNotificacionIdentifier(o1) === this.getNotificacionIdentifier(o2) : o1 === o2;
  }

  addNotificacionToCollectionIfMissing<Type extends Pick<INotificacion, 'id'>>(
    notificacionCollection: Type[],
    ...notificacionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const notificacions: Type[] = notificacionsToCheck.filter(isPresent);
    if (notificacions.length > 0) {
      const notificacionCollectionIdentifiers = notificacionCollection.map(
        notificacionItem => this.getNotificacionIdentifier(notificacionItem)!
      );
      const notificacionsToAdd = notificacions.filter(notificacionItem => {
        const notificacionIdentifier = this.getNotificacionIdentifier(notificacionItem);
        if (notificacionCollectionIdentifiers.includes(notificacionIdentifier)) {
          return false;
        }
        notificacionCollectionIdentifiers.push(notificacionIdentifier);
        return true;
      });
      return [...notificacionsToAdd, ...notificacionCollection];
    }
    return notificacionCollection;
  }
}
