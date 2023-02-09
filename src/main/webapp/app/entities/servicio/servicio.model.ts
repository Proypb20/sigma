import dayjs from 'dayjs/esm';
import { IVigilador } from 'app/entities/vigilador/vigilador.model';
import { IObjetivo } from 'app/entities/objetivo/objetivo.model';

export interface IServicio {
  id: number;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  vigilador?: Pick<IVigilador, 'id' | 'user'> | null;
  objetivo?: Pick<IObjetivo, 'id' | 'name'> | null;
}

export type NewServicio = Omit<IServicio, 'id'> & { id: null };
