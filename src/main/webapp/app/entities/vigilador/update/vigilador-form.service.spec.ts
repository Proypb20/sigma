import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../vigilador.test-samples';

import { VigiladorFormService } from './vigilador-form.service';

describe('Vigilador Form Service', () => {
  let service: VigiladorFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VigiladorFormService);
  });

  describe('Service methods', () => {
    describe('createVigiladorFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createVigiladorFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            categoria: expect.any(Object),
            objetivo: expect.any(Object),
            user: expect.any(Object),
          })
        );
      });

      it('passing IVigilador should create a new form with FormGroup', () => {
        const formGroup = service.createVigiladorFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            categoria: expect.any(Object),
            objetivo: expect.any(Object),
            user: expect.any(Object),
          })
        );
      });
    });

    describe('getVigilador', () => {
      it('should return NewVigilador for default Vigilador initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createVigiladorFormGroup(sampleWithNewData);

        const vigilador = service.getVigilador(formGroup) as any;

        expect(vigilador).toMatchObject(sampleWithNewData);
      });

      it('should return NewVigilador for empty Vigilador initial value', () => {
        const formGroup = service.createVigiladorFormGroup();

        const vigilador = service.getVigilador(formGroup) as any;

        expect(vigilador).toMatchObject({});
      });

      it('should return IVigilador', () => {
        const formGroup = service.createVigiladorFormGroup(sampleWithRequiredData);

        const vigilador = service.getVigilador(formGroup) as any;

        expect(vigilador).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IVigilador should not enable id FormControl', () => {
        const formGroup = service.createVigiladorFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewVigilador should disable id FormControl', () => {
        const formGroup = service.createVigiladorFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
