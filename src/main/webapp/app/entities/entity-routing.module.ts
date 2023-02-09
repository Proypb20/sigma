import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'categoria',
        data: { pageTitle: 'Categorias' },
        loadChildren: () => import('./categoria/categoria.module').then(m => m.CategoriaModule),
      },
      {
        path: 'objetivo',
        data: { pageTitle: 'Objetivos' },
        loadChildren: () => import('./objetivo/objetivo.module').then(m => m.ObjetivoModule),
      },
      {
        path: 'vigilador',
        data: { pageTitle: 'Vigiladores' },
        loadChildren: () => import('./vigilador/vigilador.module').then(m => m.VigiladorModule),
      },
      {
        path: 'servicio',
        data: { pageTitle: 'Servicios' },
        loadChildren: () => import('./servicio/servicio.module').then(m => m.ServicioModule),
      },
      {
        path: 'novedad',
        data: { pageTitle: 'Novedades' },
        loadChildren: () => import('./novedad/novedad.module').then(m => m.NovedadModule),
      },
      {
        path: 'notificacion',
        data: { pageTitle: 'Notificaciones' },
        loadChildren: () => import('./notificacion/notificacion.module').then(m => m.NotificacionModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
