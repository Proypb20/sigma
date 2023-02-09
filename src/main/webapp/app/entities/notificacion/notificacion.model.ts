import { INovedad } from 'app/entities/novedad/novedad.model';
import { IVigilador } from 'app/entities/vigilador/vigilador.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface INotificacion {
  id: number;
  status?: Status | null;
  novedad?: Pick<INovedad, 'id' | 'texto' | 'picture' | 'pictureContentType'> | null;
  vigilador?: Pick<IVigilador, 'id' | 'user'> | null;
}

export type NewNotificacion = Omit<INotificacion, 'id'> & { id: null };
