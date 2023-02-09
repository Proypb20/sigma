import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IVigilador } from '../vigilador.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../vigilador.test-samples';

import { VigiladorService } from './vigilador.service';

const requireRestSample: IVigilador = {
  ...sampleWithRequiredData,
};

describe('Vigilador Service', () => {
  let service: VigiladorService;
  let httpMock: HttpTestingController;
  let expectedResult: IVigilador | IVigilador[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(VigiladorService);
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

    it('should create a Vigilador', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const vigilador = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(vigilador).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Vigilador', () => {
      const vigilador = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(vigilador).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Vigilador', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Vigilador', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Vigilador', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addVigiladorToCollectionIfMissing', () => {
      it('should add a Vigilador to an empty array', () => {
        const vigilador: IVigilador = sampleWithRequiredData;
        expectedResult = service.addVigiladorToCollectionIfMissing([], vigilador);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vigilador);
      });

      it('should not add a Vigilador to an array that contains it', () => {
        const vigilador: IVigilador = sampleWithRequiredData;
        const vigiladorCollection: IVigilador[] = [
          {
            ...vigilador,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addVigiladorToCollectionIfMissing(vigiladorCollection, vigilador);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Vigilador to an array that doesn't contain it", () => {
        const vigilador: IVigilador = sampleWithRequiredData;
        const vigiladorCollection: IVigilador[] = [sampleWithPartialData];
        expectedResult = service.addVigiladorToCollectionIfMissing(vigiladorCollection, vigilador);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vigilador);
      });

      it('should add only unique Vigilador to an array', () => {
        const vigiladorArray: IVigilador[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const vigiladorCollection: IVigilador[] = [sampleWithRequiredData];
        expectedResult = service.addVigiladorToCollectionIfMissing(vigiladorCollection, ...vigiladorArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const vigilador: IVigilador = sampleWithRequiredData;
        const vigilador2: IVigilador = sampleWithPartialData;
        expectedResult = service.addVigiladorToCollectionIfMissing([], vigilador, vigilador2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vigilador);
        expect(expectedResult).toContain(vigilador2);
      });

      it('should accept null and undefined values', () => {
        const vigilador: IVigilador = sampleWithRequiredData;
        expectedResult = service.addVigiladorToCollectionIfMissing([], null, vigilador, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vigilador);
      });

      it('should return initial array if no Vigilador is added', () => {
        const vigiladorCollection: IVigilador[] = [sampleWithRequiredData];
        expectedResult = service.addVigiladorToCollectionIfMissing(vigiladorCollection, undefined, null);
        expect(expectedResult).toEqual(vigiladorCollection);
      });
    });

    describe('compareVigilador', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareVigilador(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareVigilador(entity1, entity2);
        const compareResult2 = service.compareVigilador(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareVigilador(entity1, entity2);
        const compareResult2 = service.compareVigilador(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareVigilador(entity1, entity2);
        const compareResult2 = service.compareVigilador(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
