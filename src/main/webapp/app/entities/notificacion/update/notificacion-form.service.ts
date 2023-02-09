import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { INotificacion, NewNotificacion } from '../notificacion.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts INotificacion for edit and NewNotificacionFormGroupInput for create.
 */
type NotificacionFormGroupInput = INotificacion | PartialWithRequiredKeyOf<NewNotificacion>;

type NotificacionFormDefaults = Pick<NewNotificacion, 'id'>;

type NotificacionFormGroupContent = {
  id: FormControl<INotificacion['id'] | NewNotificacion['id']>;
  status: FormControl<INotificacion['status']>;
  novedad: FormControl<INotificacion['novedad']>;
  vigilador: FormControl<INotificacion['vigilador']>;
};

export type NotificacionFormGroup = FormGroup<NotificacionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class NotificacionFormService {
  createNotificacionFormGroup(notificacion: NotificacionFormGroupInput = { id: null }): NotificacionFormGroup {
    const notificacionRawValue = {
      ...this.getFormDefaults(),
      ...notificacion,
    };
    return new FormGroup<NotificacionFormGroupContent>({
      id: new FormControl(
        { value: notificacionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      status: new FormControl(notificacionRawValue.status, {
        validators: [Validators.required],
      }),
      novedad: new FormControl(notificacionRawValue.novedad, {
        validators: [Validators.required],
      }),
      vigilador: new FormControl(notificacionRawValue.vigilador, {
        validators: [Validators.required],
      }),
    });
  }

  getNotificacion(form: NotificacionFormGroup): INotificacion | NewNotificacion {
    return form.getRawValue() as INotificacion | NewNotificacion;
  }

  resetForm(form: NotificacionFormGroup, notificacion: NotificacionFormGroupInput): void {
    const notificacionRawValue = { ...this.getFormDefaults(), ...notificacion };
    form.reset(
      {
        ...notificacionRawValue,
        id: { value: notificacionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): NotificacionFormDefaults {
    return {
      id: null,
    };
  }
}
