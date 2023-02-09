import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../objetivo.test-samples';

import { ObjetivoFormService } from './objetivo-form.service';

describe('Objetivo Form Service', () => {
  let service: ObjetivoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ObjetivoFormService);
  });

  describe('Service methods', () => {
    describe('createObjetivoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createObjetivoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            address: expect.any(Object),
            city: expect.any(Object),
          })
        );
      });

      it('passing IObjetivo should create a new form with FormGroup', () => {
        const formGroup = service.createObjetivoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            address: expect.any(Object),
            city: expect.any(Object),
          })
        );
      });
    });

    describe('getObjetivo', () => {
      it('should return NewObjetivo for default Objetivo initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createObjetivoFormGroup(sampleWithNewData);

        const objetivo = service.getObjetivo(formGroup) as any;

        expect(objetivo).toMatchObject(sampleWithNewData);
      });

      it('should return NewObjetivo for empty Objetivo initial value', () => {
        const formGroup = service.createObjetivoFormGroup();

        const objetivo = service.getObjetivo(formGroup) as any;

        expect(objetivo).toMatchObject({});
      });

      it('should return IObjetivo', () => {
        const formGroup = service.createObjetivoFormGroup(sampleWithRequiredData);

        const objetivo = service.getObjetivo(formGroup) as any;

        expect(objetivo).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IObjetivo should not enable id FormControl', () => {
        const formGroup = service.createObjetivoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewObjetivo should disable id FormControl', () => {
        const formGroup = service.createObjetivoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
