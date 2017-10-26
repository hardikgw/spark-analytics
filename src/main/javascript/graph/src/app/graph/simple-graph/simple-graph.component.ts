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

      let verticesLinkCount: Map<string, number>  = new Map();

      vertices.forEach((vertice) => {
        let node : Node = new Node(vertice.id);
        node.attr.set("name", vertice.attr);
        nodes.push(node);
        verticesLinkCount[node.id] = 0;
      });

      edges.forEach((edge)=> {
        links.push(new Link(edge.src, edge.dst));
        verticesLinkCount[edge.src] ++;
        verticesLinkCount[edge.dst] ++;
      });

      nodes.forEach((node) => {
        node.linkCount = verticesLinkCount[node.id]
      });

      this.nodes = nodes;
      this.links = links;
    });

  }

  ngOnInit() {}

}
