import { IVigilador, NewVigilador } from './vigilador.model';

export const sampleWithRequiredData: IVigilador = {
  id: 8447,
};

export const sampleWithPartialData: IVigilador = {
  id: 62098,
};

export const sampleWithFullData: IVigilador = {
  id: 99193,
};

export const sampleWithNewData: NewVigilador = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
