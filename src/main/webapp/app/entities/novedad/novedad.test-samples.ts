import { entregar } from 'app/entities/enumerations/entregar.model';

import { INovedad, NewNovedad } from './novedad.model';

export const sampleWithRequiredData: INovedad = {
  id: 68904,
  texto: 'Pequeño card',
  entregar: entregar['VIGILADOR'],
};

export const sampleWithPartialData: INovedad = {
  id: 27561,
  texto: 'integrate calculate',
  picture: '../fake-data/blob/hipster.png',
  pictureContentType: 'unknown',
  entregar: entregar['TODOS'],
};

export const sampleWithFullData: INovedad = {
  id: 52875,
  texto: 'y',
  picture: '../fake-data/blob/hipster.png',
  pictureContentType: 'unknown',
  entregar: entregar['VIGILADOR'],
};

export const sampleWithNewData: NewNovedad = {
  texto: 'tangible de Metal',
  entregar: entregar['VIGILADOR'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
