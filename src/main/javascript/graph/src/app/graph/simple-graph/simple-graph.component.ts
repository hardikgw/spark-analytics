import { Component, OnInit } from '@angular/core';
import APP_CONFIG from '../../app.config';
import { Node } from './node';
import { Link } from './link'
import { GraphframesService } from "../spark/graphframes.service";
import {Globals} from "../../globals";

@Component({
  selector: 'simple-graph',
  templateUrl: './simple-graph.component.html',
  styleUrls: ['./simple-graph.component.css']
})
export class SimpleGraphComponent implements OnInit {

  nodes: Node[] = [];
  links: Link[] = [];

  constructor(private graphService : GraphframesService, private  globalVars : Globals) {

    graphService.getData().subscribe(data=>{
      let vertices = data.vertices;

      APP_CONFIG.N = vertices.length;

      let edges = data.edges;

      let nodes: Node[] = [];
      let links: Link[] = [];

      globalVars.totalNodes = vertices.length;

      let verticesIds: Map<string, number>  = new Map();

      for(let i = 0; i < vertices.length; i++) {
        verticesIds.set(vertices[i].id, i);
        let node : Node = new Node(vertices[i].id);
        node.attr.set("name",vertices[i].attr);
        nodes.push(node);
      }

      for(let i = 0; i < edges.length; i++) {
        let source: Node = nodes[verticesIds.get(edges[i].src)];
        let target: Node = nodes[verticesIds.get(edges[i].dst)];
        if (source && target) {
          nodes[verticesIds.get(edges[i].src)].linkCount++;
          nodes[verticesIds.get(edges[i].dst)].linkCount++;
          links.push(new Link(edges[i].src, edges[i].dst));
        }
      }

      this.nodes = nodes;
      this.links = links;
    });

  }

  ngOnInit() {}

}
