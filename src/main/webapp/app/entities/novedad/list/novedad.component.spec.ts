import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { NovedadService } from '../service/novedad.service';

import { NovedadComponent } from './novedad.component';

describe('Novedad Management Component', () => {
  let comp: NovedadComponent;
  let fixture: ComponentFixture<NovedadComponent>;
  let service: NovedadService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'novedad', component: NovedadComponent }]), HttpClientTestingModule],
      declarations: [NovedadComponent],
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
      .overrideTemplate(NovedadComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(NovedadComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(NovedadService);

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
    expect(comp.novedads?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to novedadService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getNovedadIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getNovedadIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
