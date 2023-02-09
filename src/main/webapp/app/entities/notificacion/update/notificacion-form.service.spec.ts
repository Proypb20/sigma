import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../notificacion.test-samples';

import { NotificacionFormService } from './notificacion-form.service';

describe('Notificacion Form Service', () => {
  let service: NotificacionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NotificacionFormService);
  });

  describe('Service methods', () => {
    describe('createNotificacionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createNotificacionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            status: expect.any(Object),
            novedad: expect.any(Object),
            vigilador: expect.any(Object),
          })
        );
      });

      it('passing INotificacion should create a new form with FormGroup', () => {
        const formGroup = service.createNotificacionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            status: expect.any(Object),
            novedad: expect.any(Object),
            vigilador: expect.any(Object),
          })
        );
      });
    });

    describe('getNotificacion', () => {
      it('should return NewNotificacion for default Notificacion initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createNotificacionFormGroup(sampleWithNewData);

        const notificacion = service.getNotificacion(formGroup) as any;

        expect(notificacion).toMatchObject(sampleWithNewData);
      });

      it('should return NewNotificacion for empty Notificacion initial value', () => {
        const formGroup = service.createNotificacionFormGroup();

        const notificacion = service.getNotificacion(formGroup) as any;

        expect(notificacion).toMatchObject({});
      });

      it('should return INotificacion', () => {
        const formGroup = service.createNotificacionFormGroup(sampleWithRequiredData);

        const notificacion = service.getNotificacion(formGroup) as any;

        expect(notificacion).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing INotificacion should not enable id FormControl', () => {
        const formGroup = service.createNotificacionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewNotificacion should disable id FormControl', () => {
        const formGroup = service.createNotificacionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
