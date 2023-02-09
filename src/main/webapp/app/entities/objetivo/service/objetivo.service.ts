import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IObjetivo, NewObjetivo } from '../objetivo.model';

export type PartialUpdateObjetivo = Partial<IObjetivo> & Pick<IObjetivo, 'id'>;

export type EntityResponseType = HttpResponse<IObjetivo>;
export type EntityArrayResponseType = HttpResponse<IObjetivo[]>;

@Injectable({ providedIn: 'root' })
export class ObjetivoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/objetivos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(objetivo: NewObjetivo): Observable<EntityResponseType> {
    return this.http.post<IObjetivo>(this.resourceUrl, objetivo, { observe: 'response' });
  }

  update(objetivo: IObjetivo): Observable<EntityResponseType> {
    return this.http.put<IObjetivo>(`${this.resourceUrl}/${this.getObjetivoIdentifier(objetivo)}`, objetivo, { observe: 'response' });
  }

  partialUpdate(objetivo: PartialUpdateObjetivo): Observable<EntityResponseType> {
    return this.http.patch<IObjetivo>(`${this.resourceUrl}/${this.getObjetivoIdentifier(objetivo)}`, objetivo, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IObjetivo>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IObjetivo[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getObjetivoIdentifier(objetivo: Pick<IObjetivo, 'id'>): number {
    return objetivo.id;
  }

  compareObjetivo(o1: Pick<IObjetivo, 'id'> | null, o2: Pick<IObjetivo, 'id'> | null): boolean {
    return o1 && o2 ? this.getObjetivoIdentifier(o1) === this.getObjetivoIdentifier(o2) : o1 === o2;
  }

  addObjetivoToCollectionIfMissing<Type extends Pick<IObjetivo, 'id'>>(
    objetivoCollection: Type[],
    ...objetivosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const objetivos: Type[] = objetivosToCheck.filter(isPresent);
    if (objetivos.length > 0) {
      const objetivoCollectionIdentifiers = objetivoCollection.map(objetivoItem => this.getObjetivoIdentifier(objetivoItem)!);
      const objetivosToAdd = objetivos.filter(objetivoItem => {
        const objetivoIdentifier = this.getObjetivoIdentifier(objetivoItem);
        if (objetivoCollectionIdentifiers.includes(objetivoIdentifier)) {
          return false;
        }
        objetivoCollectionIdentifiers.push(objetivoIdentifier);
        return true;
      });
      return [...objetivosToAdd, ...objetivoCollection];
    }
    return objetivoCollection;
  }
}
