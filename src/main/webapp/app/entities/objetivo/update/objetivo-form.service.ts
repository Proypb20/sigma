import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IObjetivo, NewObjetivo } from '../objetivo.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IObjetivo for edit and NewObjetivoFormGroupInput for create.
 */
type ObjetivoFormGroupInput = IObjetivo | PartialWithRequiredKeyOf<NewObjetivo>;

type ObjetivoFormDefaults = Pick<NewObjetivo, 'id'>;

type ObjetivoFormGroupContent = {
  id: FormControl<IObjetivo['id'] | NewObjetivo['id']>;
  name: FormControl<IObjetivo['name']>;
  address: FormControl<IObjetivo['address']>;
  city: FormControl<IObjetivo['city']>;
};

export type ObjetivoFormGroup = FormGroup<ObjetivoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ObjetivoFormService {
  createObjetivoFormGroup(objetivo: ObjetivoFormGroupInput = { id: null }): ObjetivoFormGroup {
    const objetivoRawValue = {
      ...this.getFormDefaults(),
      ...objetivo,
    };
    return new FormGroup<ObjetivoFormGroupContent>({
      id: new FormControl(
        { value: objetivoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(objetivoRawValue.name, {
        validators: [Validators.required],
      }),
      address: new FormControl(objetivoRawValue.address),
      city: new FormControl(objetivoRawValue.city),
    });
  }

  getObjetivo(form: ObjetivoFormGroup): IObjetivo | NewObjetivo {
    return form.getRawValue() as IObjetivo | NewObjetivo;
  }

  resetForm(form: ObjetivoFormGroup, objetivo: ObjetivoFormGroupInput): void {
    const objetivoRawValue = { ...this.getFormDefaults(), ...objetivo };
    form.reset(
      {
        ...objetivoRawValue,
        id: { value: objetivoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ObjetivoFormDefaults {
    return {
      id: null,
    };
  }
}
