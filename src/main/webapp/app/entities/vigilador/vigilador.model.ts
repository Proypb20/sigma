import { ICategoria } from 'app/entities/categoria/categoria.model';
import { IObjetivo } from 'app/entities/objetivo/objetivo.model';
import { IUser } from 'app/entities/user/user.model';

export interface IVigilador {
  id: number;
  categoria?: Pick<ICategoria, 'id' | 'name'> | null;
  objetivo?: Pick<IObjetivo, 'id' | 'name'> | null;
  user?: Pick<IUser, 'id' | 'login' | 'lastName' | 'firstName'> | null;
}

export type NewVigilador = Omit<IVigilador, 'id'> & { id: null };
