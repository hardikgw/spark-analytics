import { Component, OnInit } from '@angular/core';
import APP_CONFIG from '../../app.config';
import { Node } from './node';
import { Link } from './link'
import { GraphframesService } from "../spark/graphframes.service";

@Component({
  selector: 'simple-graph',
  templateUrl: './simple-graph.component.html',
  styleUrls: ['./simple-graph.component.css'],
  providers:[GraphframesService]
})
export class SimpleGraphComponent implements OnInit {

  nodes: Node[] = [];
  links: Link[] = [];

  constructor(private graphService : GraphframesService) {

    const N = APP_CONFIG.N,
      getIndex = number => number - 1;

    /** constructing the nodes array */
    for (let i = 1; i <= N; i++) {
      this.nodes.push(new Node(i));
    }

    for (let i = 1; i <= N; i++) {
      for (let m = 2; i * m <= N; m++) {
        /** increasing connections toll on connecting nodes */
        this.nodes[getIndex(i)].linkCount++;
        this.nodes[getIndex(i * m)].linkCount++;

        /** connecting the nodes before starting the simulation */
        this.links.push(new Link(i, i * m));
      }
    }

    graphService.nodes();

  }

  ngOnInit() {
  }

}
