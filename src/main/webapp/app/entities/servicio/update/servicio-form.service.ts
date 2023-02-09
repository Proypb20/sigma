import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IServicio, NewServicio } from '../servicio.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IServicio for edit and NewServicioFormGroupInput for create.
 */
type ServicioFormGroupInput = IServicio | PartialWithRequiredKeyOf<NewServicio>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IServicio | NewServicio> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

type ServicioFormRawValue = FormValueOf<IServicio>;

type NewServicioFormRawValue = FormValueOf<NewServicio>;

type ServicioFormDefaults = Pick<NewServicio, 'id' | 'startDate' | 'endDate'>;

type ServicioFormGroupContent = {
  id: FormControl<ServicioFormRawValue['id'] | NewServicio['id']>;
  startDate: FormControl<ServicioFormRawValue['startDate']>;
  endDate: FormControl<ServicioFormRawValue['endDate']>;
  vigilador: FormControl<ServicioFormRawValue['vigilador']>;
  objetivo: FormControl<ServicioFormRawValue['objetivo']>;
};

export type ServicioFormGroup = FormGroup<ServicioFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ServicioFormService {
  createServicioFormGroup(servicio: ServicioFormGroupInput = { id: null }): ServicioFormGroup {
    const servicioRawValue = this.convertServicioToServicioRawValue({
      ...this.getFormDefaults(),
      ...servicio,
    });
    return new FormGroup<ServicioFormGroupContent>({
      id: new FormControl(
        { value: servicioRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      startDate: new FormControl(servicioRawValue.startDate, {
        validators: [Validators.required],
      }),
      endDate: new FormControl(servicioRawValue.endDate),
      vigilador: new FormControl(servicioRawValue.vigilador, {
        validators: [Validators.required],
      }),
      objetivo: new FormControl(servicioRawValue.objetivo, {
        validators: [Validators.required],
      }),
    });
  }

  getServicio(form: ServicioFormGroup): IServicio | NewServicio {
    return this.convertServicioRawValueToServicio(form.getRawValue() as ServicioFormRawValue | NewServicioFormRawValue);
  }

  resetForm(form: ServicioFormGroup, servicio: ServicioFormGroupInput): void {
    const servicioRawValue = this.convertServicioToServicioRawValue({ ...this.getFormDefaults(), ...servicio });
    form.reset(
      {
        ...servicioRawValue,
        id: { value: servicioRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ServicioFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startDate: currentTime,
      endDate: currentTime,
    };
  }

  private convertServicioRawValueToServicio(rawServicio: ServicioFormRawValue | NewServicioFormRawValue): IServicio | NewServicio {
    return {
      ...rawServicio,
      startDate: dayjs(rawServicio.startDate, DATE_TIME_FORMAT),
      endDate: dayjs(rawServicio.endDate, DATE_TIME_FORMAT),
    };
  }

  private convertServicioToServicioRawValue(
    servicio: IServicio | (Partial<NewServicio> & ServicioFormDefaults)
  ): ServicioFormRawValue | PartialWithRequiredKeyOf<NewServicioFormRawValue> {
    return {
      ...servicio,
      startDate: servicio.startDate ? servicio.startDate.format(DATE_TIME_FORMAT) : undefined,
      endDate: servicio.endDate ? servicio.endDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
