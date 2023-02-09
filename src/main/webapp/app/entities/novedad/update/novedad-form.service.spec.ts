import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../novedad.test-samples';

import { NovedadFormService } from './novedad-form.service';

describe('Novedad Form Service', () => {
  let service: NovedadFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NovedadFormService);
  });

  describe('Service methods', () => {
    describe('createNovedadFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createNovedadFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            texto: expect.any(Object),
            picture: expect.any(Object),
            entregar: expect.any(Object),
            vigilador: expect.any(Object),
            objetivo: expect.any(Object),
          })
        );
      });

      it('passing INovedad should create a new form with FormGroup', () => {
        const formGroup = service.createNovedadFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            texto: expect.any(Object),
            picture: expect.any(Object),
            entregar: expect.any(Object),
            vigilador: expect.any(Object),
            objetivo: expect.any(Object),
          })
        );
      });
    });

    describe('getNovedad', () => {
      it('should return NewNovedad for default Novedad initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createNovedadFormGroup(sampleWithNewData);

        const novedad = service.getNovedad(formGroup) as any;

        expect(novedad).toMatchObject(sampleWithNewData);
      });

      it('should return NewNovedad for empty Novedad initial value', () => {
        const formGroup = service.createNovedadFormGroup();

        const novedad = service.getNovedad(formGroup) as any;

        expect(novedad).toMatchObject({});
      });

      it('should return INovedad', () => {
        const formGroup = service.createNovedadFormGroup(sampleWithRequiredData);

        const novedad = service.getNovedad(formGroup) as any;

        expect(novedad).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing INovedad should not enable id FormControl', () => {
        const formGroup = service.createNovedadFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewNovedad should disable id FormControl', () => {
        const formGroup = service.createNovedadFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
