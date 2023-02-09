import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IVigilador, NewVigilador } from '../vigilador.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVigilador for edit and NewVigiladorFormGroupInput for create.
 */
type VigiladorFormGroupInput = IVigilador | PartialWithRequiredKeyOf<NewVigilador>;

type VigiladorFormDefaults = Pick<NewVigilador, 'id'>;

type VigiladorFormGroupContent = {
  id: FormControl<IVigilador['id'] | NewVigilador['id']>;
  categoria: FormControl<IVigilador['categoria']>;
  objetivo: FormControl<IVigilador['objetivo']>;
  user: FormControl<IVigilador['user']>;
};

export type VigiladorFormGroup = FormGroup<VigiladorFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VigiladorFormService {
  createVigiladorFormGroup(vigilador: VigiladorFormGroupInput = { id: null }): VigiladorFormGroup {
    const vigiladorRawValue = {
      ...this.getFormDefaults(),
      ...vigilador,
    };
    return new FormGroup<VigiladorFormGroupContent>({
      id: new FormControl(
        { value: vigiladorRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      categoria: new FormControl(vigiladorRawValue.categoria),
      objetivo: new FormControl(vigiladorRawValue.objetivo),
      user: new FormControl(vigiladorRawValue.user),
    });
  }

  getVigilador(form: VigiladorFormGroup): IVigilador | NewVigilador {
    return form.getRawValue() as IVigilador | NewVigilador;
  }

  resetForm(form: VigiladorFormGroup, vigilador: VigiladorFormGroupInput): void {
    const vigiladorRawValue = { ...this.getFormDefaults(), ...vigilador };
    form.reset(
      {
        ...vigiladorRawValue,
        id: { value: vigiladorRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): VigiladorFormDefaults {
    return {
      id: null,
    };
  }
}
