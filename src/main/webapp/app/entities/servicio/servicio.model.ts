import dayjs from 'dayjs/esm';
import { IVigilador } from 'app/entities/vigilador/vigilador.model';

export interface IServicio {
  id: number;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  vigilador?: Pick<IVigilador, 'id' | 'user'> | null;
}

export type NewServicio = Omit<IServicio, 'id'> & { id: null };
