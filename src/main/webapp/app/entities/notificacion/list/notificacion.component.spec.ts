import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { NotificacionService } from '../service/notificacion.service';

import { NotificacionComponent } from './notificacion.component';

describe('Notificacion Management Component', () => {
  let comp: NotificacionComponent;
  let fixture: ComponentFixture<NotificacionComponent>;
  let service: NotificacionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'notificacion', component: NotificacionComponent }]), HttpClientTestingModule],
      declarations: [NotificacionComponent],
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
      .overrideTemplate(NotificacionComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(NotificacionComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(NotificacionService);

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
    expect(comp.notificacions?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to notificacionService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getNotificacionIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getNotificacionIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
