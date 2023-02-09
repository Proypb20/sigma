import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { VigiladorService } from '../service/vigilador.service';

import { VigiladorComponent } from './vigilador.component';

describe('Vigilador Management Component', () => {
  let comp: VigiladorComponent;
  let fixture: ComponentFixture<VigiladorComponent>;
  let service: VigiladorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'vigilador', component: VigiladorComponent }]), HttpClientTestingModule],
      declarations: [VigiladorComponent],
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
      .overrideTemplate(VigiladorComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VigiladorComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(VigiladorService);

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
    expect(comp.vigiladors?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to vigiladorService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getVigiladorIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getVigiladorIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
