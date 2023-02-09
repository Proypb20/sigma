import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVigilador, NewVigilador } from '../vigilador.model';

export type PartialUpdateVigilador = Partial<IVigilador> & Pick<IVigilador, 'id'>;

export type EntityResponseType = HttpResponse<IVigilador>;
export type EntityArrayResponseType = HttpResponse<IVigilador[]>;

@Injectable({ providedIn: 'root' })
export class VigiladorService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/vigiladors');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(vigilador: NewVigilador): Observable<EntityResponseType> {
    return this.http.post<IVigilador>(this.resourceUrl, vigilador, { observe: 'response' });
  }

  update(vigilador: IVigilador): Observable<EntityResponseType> {
    return this.http.put<IVigilador>(`${this.resourceUrl}/${this.getVigiladorIdentifier(vigilador)}`, vigilador, { observe: 'response' });
  }

  partialUpdate(vigilador: PartialUpdateVigilador): Observable<EntityResponseType> {
    return this.http.patch<IVigilador>(`${this.resourceUrl}/${this.getVigiladorIdentifier(vigilador)}`, vigilador, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVigilador>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVigilador[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getVigiladorIdentifier(vigilador: Pick<IVigilador, 'id'>): number {
    return vigilador.id;
  }

  compareVigilador(o1: Pick<IVigilador, 'id'> | null, o2: Pick<IVigilador, 'id'> | null): boolean {
    return o1 && o2 ? this.getVigiladorIdentifier(o1) === this.getVigiladorIdentifier(o2) : o1 === o2;
  }

  addVigiladorToCollectionIfMissing<Type extends Pick<IVigilador, 'id'>>(
    vigiladorCollection: Type[],
    ...vigiladorsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const vigiladors: Type[] = vigiladorsToCheck.filter(isPresent);
    if (vigiladors.length > 0) {
      const vigiladorCollectionIdentifiers = vigiladorCollection.map(vigiladorItem => this.getVigiladorIdentifier(vigiladorItem)!);
      const vigiladorsToAdd = vigiladors.filter(vigiladorItem => {
        const vigiladorIdentifier = this.getVigiladorIdentifier(vigiladorItem);
        if (vigiladorCollectionIdentifiers.includes(vigiladorIdentifier)) {
          return false;
        }
        vigiladorCollectionIdentifiers.push(vigiladorIdentifier);
        return true;
      });
      return [...vigiladorsToAdd, ...vigiladorCollection];
    }
    return vigiladorCollection;
  }
}
