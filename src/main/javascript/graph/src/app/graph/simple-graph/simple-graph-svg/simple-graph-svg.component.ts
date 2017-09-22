import {Component, Input, ChangeDetectorRef, HostListener, ChangeDetectionStrategy, OnInit} from '@angular/core';
import { SimpleGraph} from "../simple-graph";
import { SimpleGraphService } from "../simple-graph.service";
import { SimpleGraphLinkSvgComponent} from "../simple-graph-link-svg/simple-graph-link-svg.component";
import { Node } from '../node'

@Component({
  selector: 'simple-graph-svg',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './simple-graph-svg.component.html',
  styleUrls: ['./simple-graph-svg.component.css']
})
export class SimpleGraphSvgComponent implements OnInit {

  @Input('nodes') nodes;
  @Input('links') links;

  @HostListener('window:resize', ['$event'])
  onResize(event) {
    this.graph.initSimulation(this.options);
  }

  graph: SimpleGraph;

  constructor(private service: SimpleGraphService, private ref: ChangeDetectorRef) { }

  ngOnInit() {
    /** Receiving an initialized simulated graph from our custom d3 service */
    this.graph = this.service.getForceDirectedGraph(this.nodes, this.links, this.options);

    /** Binding change detection check on each tick
     * This along with an onPush change detection strategy should enforce checking only when relevant!
     * This improves scripting computation duration in a couple of tests I've made, consistently.
     * Also, it makes sense to avoid unnecessary checks when we are dealing only with simulations data binding.
     */
    this.graph.ticker.subscribe((d) => {
      this.ref.markForCheck();
    });
  }

  ngAfterViewInit() {
    this.graph.initSimulation(this.options);
  }

  private _options: { width, height } = { width: 800, height: 600 };

  get options() {
    return this._options = {
      width: window.innerWidth,
      height: window.innerHeight
    };
  }

}
