export interface IObjetivo {
  id: number;
  name?: string | null;
  address?: string | null;
  city?: string | null;
}

export type NewObjetivo = Omit<IObjetivo, 'id'> & { id: null };
