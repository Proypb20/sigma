export interface ICategoria {
  id: number;
  name?: string | null;
  hourValue?: number | null;
  extra50?: number | null;
  extra100?: number | null;
  totalHour?: number | null;
}

export type NewCategoria = Omit<ICategoria, 'id'> & { id: null };
