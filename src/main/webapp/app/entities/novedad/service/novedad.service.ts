import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { INovedad, NewNovedad } from '../novedad.model';

export type PartialUpdateNovedad = Partial<INovedad> & Pick<INovedad, 'id'>;

export type EntityResponseType = HttpResponse<INovedad>;
export type EntityArrayResponseType = HttpResponse<INovedad[]>;

@Injectable({ providedIn: 'root' })
export class NovedadService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/novedads');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(novedad: NewNovedad): Observable<EntityResponseType> {
    return this.http.post<INovedad>(this.resourceUrl, novedad, { observe: 'response' });
  }

  update(novedad: INovedad): Observable<EntityResponseType> {
    return this.http.put<INovedad>(`${this.resourceUrl}/${this.getNovedadIdentifier(novedad)}`, novedad, { observe: 'response' });
  }

  partialUpdate(novedad: PartialUpdateNovedad): Observable<EntityResponseType> {
    return this.http.patch<INovedad>(`${this.resourceUrl}/${this.getNovedadIdentifier(novedad)}`, novedad, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<INovedad>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<INovedad[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getNovedadIdentifier(novedad: Pick<INovedad, 'id'>): number {
    return novedad.id;
  }

  compareNovedad(o1: Pick<INovedad, 'id'> | null, o2: Pick<INovedad, 'id'> | null): boolean {
    return o1 && o2 ? this.getNovedadIdentifier(o1) === this.getNovedadIdentifier(o2) : o1 === o2;
  }

  addNovedadToCollectionIfMissing<Type extends Pick<INovedad, 'id'>>(
    novedadCollection: Type[],
    ...novedadsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const novedads: Type[] = novedadsToCheck.filter(isPresent);
    if (novedads.length > 0) {
      const novedadCollectionIdentifiers = novedadCollection.map(novedadItem => this.getNovedadIdentifier(novedadItem)!);
      const novedadsToAdd = novedads.filter(novedadItem => {
        const novedadIdentifier = this.getNovedadIdentifier(novedadItem);
        if (novedadCollectionIdentifiers.includes(novedadIdentifier)) {
          return false;
        }
        novedadCollectionIdentifiers.push(novedadIdentifier);
        return true;
      });
      return [...novedadsToAdd, ...novedadCollection];
    }
    return novedadCollection;
  }
}
