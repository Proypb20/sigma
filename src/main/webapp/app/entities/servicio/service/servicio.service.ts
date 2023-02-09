import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IServicio, NewServicio } from '../servicio.model';

export type PartialUpdateServicio = Partial<IServicio> & Pick<IServicio, 'id'>;

type RestOf<T extends IServicio | NewServicio> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

export type RestServicio = RestOf<IServicio>;

export type NewRestServicio = RestOf<NewServicio>;

export type PartialUpdateRestServicio = RestOf<PartialUpdateServicio>;

export type EntityResponseType = HttpResponse<IServicio>;
export type EntityArrayResponseType = HttpResponse<IServicio[]>;

@Injectable({ providedIn: 'root' })
export class ServicioService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/servicios');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(servicio: NewServicio): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(servicio);
    return this.http
      .post<RestServicio>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(servicio: IServicio): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(servicio);
    return this.http
      .put<RestServicio>(`${this.resourceUrl}/${this.getServicioIdentifier(servicio)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(servicio: PartialUpdateServicio): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(servicio);
    return this.http
      .patch<RestServicio>(`${this.resourceUrl}/${this.getServicioIdentifier(servicio)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestServicio>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestServicio[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getServicioIdentifier(servicio: Pick<IServicio, 'id'>): number {
    return servicio.id;
  }

  takeService(vId: number): Observable<EntityResponseType> {
    return this.http
      .post<RestServicio>(`${this.resourceUrl}/takeService`, vId, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  leftService(servicio: IServicio): Observable<EntityResponseType> {
    return this.http
      .post<RestServicio>(`${this.resourceUrl}/leftService`, servicio, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  compareServicio(o1: Pick<IServicio, 'id'> | null, o2: Pick<IServicio, 'id'> | null): boolean {
    return o1 && o2 ? this.getServicioIdentifier(o1) === this.getServicioIdentifier(o2) : o1 === o2;
  }

  addServicioToCollectionIfMissing<Type extends Pick<IServicio, 'id'>>(
    servicioCollection: Type[],
    ...serviciosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const servicios: Type[] = serviciosToCheck.filter(isPresent);
    if (servicios.length > 0) {
      const servicioCollectionIdentifiers = servicioCollection.map(servicioItem => this.getServicioIdentifier(servicioItem)!);
      const serviciosToAdd = servicios.filter(servicioItem => {
        const servicioIdentifier = this.getServicioIdentifier(servicioItem);
        if (servicioCollectionIdentifiers.includes(servicioIdentifier)) {
          return false;
        }
        servicioCollectionIdentifiers.push(servicioIdentifier);
        return true;
      });
      return [...serviciosToAdd, ...servicioCollection];
    }
    return servicioCollection;
  }

  protected convertDateFromClient<T extends IServicio | NewServicio | PartialUpdateServicio>(servicio: T): RestOf<T> {
    return {
      ...servicio,
      startDate: servicio.startDate?.toJSON() ?? null,
      endDate: servicio.endDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restServicio: RestServicio): IServicio {
    return {
      ...restServicio,
      startDate: restServicio.startDate ? dayjs(restServicio.startDate) : undefined,
      endDate: restServicio.endDate ? dayjs(restServicio.endDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestServicio>): HttpResponse<IServicio> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestServicio[]>): HttpResponse<IServicio[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
