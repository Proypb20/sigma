import { ICategoria, NewCategoria } from './categoria.model';

export const sampleWithRequiredData: ICategoria = {
  id: 34642,
  name: 'deposit',
};

export const sampleWithPartialData: ICategoria = {
  id: 33522,
  name: 'withdrawal',
  totalHour: 65789,
};

export const sampleWithFullData: ICategoria = {
  id: 21401,
  name: 'Especialista Buckinghamshire',
  hourValue: 42078,
  extra50: 24550,
  extra100: 89752,
  totalHour: 83371,
};

export const sampleWithNewData: NewCategoria = {
  name: 'Seguro',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
