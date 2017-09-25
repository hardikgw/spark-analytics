import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {GraphComponent} from "./graph/graph.component";

const routes: Routes = [
  {
    path: 'graph',
    component: GraphComponent
  },
  {
    path: '',
    redirectTo : '/graph',
    pathMatch: 'full'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)]
})
export class AppRoutingModule { }
