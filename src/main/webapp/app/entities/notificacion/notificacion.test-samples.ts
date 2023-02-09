import { Status } from 'app/entities/enumerations/status.model';

import { INotificacion, NewNotificacion } from './notificacion.model';

export const sampleWithRequiredData: INotificacion = {
  id: 35948,
  status: Status['PENDIENTE'],
};

export const sampleWithPartialData: INotificacion = {
  id: 32803,
  status: Status['LEIDO'],
};

export const sampleWithFullData: INotificacion = {
  id: 90716,
  status: Status['PENDIENTE'],
};

export const sampleWithNewData: NewNotificacion = {
  status: Status['LEIDO'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
