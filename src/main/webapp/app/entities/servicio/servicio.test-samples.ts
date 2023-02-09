import dayjs from 'dayjs/esm';

import { IServicio, NewServicio } from './servicio.model';

export const sampleWithRequiredData: IServicio = {
  id: 56626,
  startDate: dayjs('2023-01-19T19:07'),
};

export const sampleWithPartialData: IServicio = {
  id: 34247,
  startDate: dayjs('2023-01-20T08:14'),
  endDate: dayjs('2023-01-19T17:57'),
};

export const sampleWithFullData: IServicio = {
  id: 28579,
  startDate: dayjs('2023-01-20T13:57'),
  endDate: dayjs('2023-01-20T02:49'),
};

export const sampleWithNewData: NewServicio = {
  startDate: dayjs('2023-01-20T12:57'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
