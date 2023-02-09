import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICategoria, NewCategoria } from '../categoria.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICategoria for edit and NewCategoriaFormGroupInput for create.
 */
type CategoriaFormGroupInput = ICategoria | PartialWithRequiredKeyOf<NewCategoria>;

type CategoriaFormDefaults = Pick<NewCategoria, 'id'>;

type CategoriaFormGroupContent = {
  id: FormControl<ICategoria['id'] | NewCategoria['id']>;
  name: FormControl<ICategoria['name']>;
  hourValue: FormControl<ICategoria['hourValue']>;
  extra50: FormControl<ICategoria['extra50']>;
  extra100: FormControl<ICategoria['extra100']>;
  totalHour: FormControl<ICategoria['totalHour']>;
};

export type CategoriaFormGroup = FormGroup<CategoriaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CategoriaFormService {
  createCategoriaFormGroup(categoria: CategoriaFormGroupInput = { id: null }): CategoriaFormGroup {
    const categoriaRawValue = {
      ...this.getFormDefaults(),
      ...categoria,
    };
    return new FormGroup<CategoriaFormGroupContent>({
      id: new FormControl(
        { value: categoriaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(categoriaRawValue.name, {
        validators: [Validators.required],
      }),
      hourValue: new FormControl(categoriaRawValue.hourValue),
      extra50: new FormControl(categoriaRawValue.extra50),
      extra100: new FormControl(categoriaRawValue.extra100),
      totalHour: new FormControl(categoriaRawValue.totalHour),
    });
  }

  getCategoria(form: CategoriaFormGroup): ICategoria | NewCategoria {
    return form.getRawValue() as ICategoria | NewCategoria;
  }

  resetForm(form: CategoriaFormGroup, categoria: CategoriaFormGroupInput): void {
    const categoriaRawValue = { ...this.getFormDefaults(), ...categoria };
    form.reset(
      {
        ...categoriaRawValue,
        id: { value: categoriaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CategoriaFormDefaults {
    return {
      id: null,
    };
  }
}
