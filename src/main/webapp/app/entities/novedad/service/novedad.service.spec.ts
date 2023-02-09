import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { INovedad } from '../novedad.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../novedad.test-samples';

import { NovedadService } from './novedad.service';

const requireRestSample: INovedad = {
  ...sampleWithRequiredData,
};

describe('Novedad Service', () => {
  let service: NovedadService;
  let httpMock: HttpTestingController;
  let expectedResult: INovedad | INovedad[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(NovedadService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Novedad', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const novedad = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(novedad).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Novedad', () => {
      const novedad = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(novedad).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Novedad', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Novedad', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Novedad', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addNovedadToCollectionIfMissing', () => {
      it('should add a Novedad to an empty array', () => {
        const novedad: INovedad = sampleWithRequiredData;
        expectedResult = service.addNovedadToCollectionIfMissing([], novedad);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(novedad);
      });

      it('should not add a Novedad to an array that contains it', () => {
        const novedad: INovedad = sampleWithRequiredData;
        const novedadCollection: INovedad[] = [
          {
            ...novedad,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addNovedadToCollectionIfMissing(novedadCollection, novedad);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Novedad to an array that doesn't contain it", () => {
        const novedad: INovedad = sampleWithRequiredData;
        const novedadCollection: INovedad[] = [sampleWithPartialData];
        expectedResult = service.addNovedadToCollectionIfMissing(novedadCollection, novedad);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(novedad);
      });

      it('should add only unique Novedad to an array', () => {
        const novedadArray: INovedad[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const novedadCollection: INovedad[] = [sampleWithRequiredData];
        expectedResult = service.addNovedadToCollectionIfMissing(novedadCollection, ...novedadArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const novedad: INovedad = sampleWithRequiredData;
        const novedad2: INovedad = sampleWithPartialData;
        expectedResult = service.addNovedadToCollectionIfMissing([], novedad, novedad2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(novedad);
        expect(expectedResult).toContain(novedad2);
      });

      it('should accept null and undefined values', () => {
        const novedad: INovedad = sampleWithRequiredData;
        expectedResult = service.addNovedadToCollectionIfMissing([], null, novedad, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(novedad);
      });

      it('should return initial array if no Novedad is added', () => {
        const novedadCollection: INovedad[] = [sampleWithRequiredData];
        expectedResult = service.addNovedadToCollectionIfMissing(novedadCollection, undefined, null);
        expect(expectedResult).toEqual(novedadCollection);
      });
    });

    describe('compareNovedad', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareNovedad(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareNovedad(entity1, entity2);
        const compareResult2 = service.compareNovedad(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareNovedad(entity1, entity2);
        const compareResult2 = service.compareNovedad(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareNovedad(entity1, entity2);
        const compareResult2 = service.compareNovedad(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
