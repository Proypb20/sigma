import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { INovedad, NewNovedad } from '../novedad.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts INovedad for edit and NewNovedadFormGroupInput for create.
 */
type NovedadFormGroupInput = INovedad | PartialWithRequiredKeyOf<NewNovedad>;

type NovedadFormDefaults = Pick<NewNovedad, 'id'>;

type NovedadFormGroupContent = {
  id: FormControl<INovedad['id'] | NewNovedad['id']>;
  texto: FormControl<INovedad['texto']>;
  picture: FormControl<INovedad['picture']>;
  pictureContentType: FormControl<INovedad['pictureContentType']>;
  entregar: FormControl<INovedad['entregar']>;
  vigilador: FormControl<INovedad['vigilador']>;
  objetivo: FormControl<INovedad['objetivo']>;
};

export type NovedadFormGroup = FormGroup<NovedadFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class NovedadFormService {
  createNovedadFormGroup(novedad: NovedadFormGroupInput = { id: null }): NovedadFormGroup {
    const novedadRawValue = {
      ...this.getFormDefaults(),
      ...novedad,
    };
    return new FormGroup<NovedadFormGroupContent>({
      id: new FormControl(
        { value: novedadRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      texto: new FormControl(novedadRawValue.texto, {
        validators: [Validators.required],
      }),
      picture: new FormControl(novedadRawValue.picture),
      pictureContentType: new FormControl(novedadRawValue.pictureContentType),
      entregar: new FormControl(novedadRawValue.entregar, {
        validators: [Validators.required],
      }),
      vigilador: new FormControl(novedadRawValue.vigilador),
      objetivo: new FormControl(novedadRawValue.objetivo),
    });
  }

  getNovedad(form: NovedadFormGroup): INovedad | NewNovedad {
    return form.getRawValue() as INovedad | NewNovedad;
  }

  resetForm(form: NovedadFormGroup, novedad: NovedadFormGroupInput): void {
    const novedadRawValue = { ...this.getFormDefaults(), ...novedad };
    form.reset(
      {
        ...novedadRawValue,
        id: { value: novedadRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): NovedadFormDefaults {
    return {
      id: null,
    };
  }
}
