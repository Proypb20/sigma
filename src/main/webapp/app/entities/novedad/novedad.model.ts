import { IVigilador } from 'app/entities/vigilador/vigilador.model';
import { IObjetivo } from 'app/entities/objetivo/objetivo.model';
import { entregar } from 'app/entities/enumerations/entregar.model';

export interface INovedad {
  id: number;
  texto?: string | null;
  picture?: string | null;
  pictureContentType?: string | null;
  entregar?: entregar | null;
  vigilador?: Pick<IVigilador, 'id'> | null;
  objetivo?: Pick<IObjetivo, 'id'> | null;
}

export type NewNovedad = Omit<INovedad, 'id'> & { id: null };
