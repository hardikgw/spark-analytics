import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MdToolbarModule } from '@angular/material';
import { AppComponent } from './app.component';
import { RouterModule } from '@angular/router';
import { SimpleGraphComponent } from './graph/simple-graph/simple-graph.component';
import { GraphComponent } from './graph/graph.component';
import { AppRoutingModule } from "./app-routing.module";
import { DraggableDirective } from './graph/simple-graph/draggable.directive';
import { ZoomableDirective } from './graph/simple-graph/zoomable.directive';
import { SimpleGraphSvgComponent } from './graph/simple-graph/simple-graph-svg/simple-graph-svg.component';
import { SimpleGraphNodeSvgComponent } from './graph/simple-graph/simple-graph-node-svg/simple-graph-node-svg.component';
import { SimpleGraphLinkSvgComponent } from './graph/simple-graph/simple-graph-link-svg/simple-graph-link-svg.component';
import {SimpleGraphService} from "./graph/simple-graph/simple-graph.service";

@NgModule({
  declarations: [
    AppComponent,
    SimpleGraphComponent,
    GraphComponent,
    DraggableDirective,
    ZoomableDirective,
    SimpleGraphSvgComponent,
    SimpleGraphNodeSvgComponent,
    SimpleGraphLinkSvgComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    MdToolbarModule,
    RouterModule,
    AppRoutingModule
  ],
  providers: [SimpleGraphService],
  bootstrap: [AppComponent]
})

export class AppModule { }
