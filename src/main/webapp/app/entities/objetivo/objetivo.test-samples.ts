import { IObjetivo, NewObjetivo } from './objetivo.model';

export const sampleWithRequiredData: IObjetivo = {
  id: 90803,
  name: 'Asociado',
};

export const sampleWithPartialData: IObjetivo = {
  id: 89621,
  name: 'Comunidad',
  address: 'Personal',
  city: 'Pinedahaven',
};

export const sampleWithFullData: IObjetivo = {
  id: 2489,
  name: 'connect',
  address: 'AI',
  city: 'Nampa',
};

export const sampleWithNewData: NewObjetivo = {
  name: 'Madera',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
