import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ObjetivoService } from '../service/objetivo.service';

import { ObjetivoComponent } from './objetivo.component';

describe('Objetivo Management Component', () => {
  let comp: ObjetivoComponent;
  let fixture: ComponentFixture<ObjetivoComponent>;
  let service: ObjetivoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'objetivo', component: ObjetivoComponent }]), HttpClientTestingModule],
      declarations: [ObjetivoComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
                'filter[someId.in]': 'dc4279ea-cfb9-11ec-9d64-0242ac120002',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(ObjetivoComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ObjetivoComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ObjetivoService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.objetivos?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to objetivoService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getObjetivoIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getObjetivoIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
