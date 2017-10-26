import { Injectable, EventEmitter } from '@angular/core';
import { Node } from './node'
import { Link } from './link'
import { SimpleGraph} from "./simple-graph";
import {GraphframesService} from "../spark/graphframes.service";
import * as d3 from 'd3';

@Injectable()
export class SimpleGraphService {

  constructor(private sparkService:GraphframesService) { }

  /** A method to highlight lined edges of selected node on click */
  applyClickableBehaviour(element, node: Node, graph: SimpleGraph) {
    let d3element = d3.select(element);
    let svg = d3.select(element.parentElement).selectAll("line").data(graph.links);
    let sparkService = this.sparkService;
    function clicked() {
      svg.style('stroke', o => {
        return (o.source === node|| o.target === node ? "red" : "rgb(222,237,250)")
      });
      sparkService.getConnectedGraph(node.id).subscribe(data=> {
        let vertices = data.vertices;
        let edges = data.edges;
        vertices.forEach((vertice)=>{
          graph.nodes.push(new Node(vertice.id))
        });
        edges.forEach((edge)=> {
          graph.links.push(new Link(edge.src, edge.dst))
        });
        console.log(edges);
        console.log(vertices);
      });
      console.log("clicked" + node.id);
    }
    d3element.on("click", clicked);
  }

  /** A method to release node on double click */
  applyDoubleClickableBehaviour(element, node: Node, graph: SimpleGraph) {
    let d3element = d3.select(element);

    function dblclick() {
        node.fx = null;
        node.fy = null;
    }
    d3element.on("dblclick", dblclick);
  }

  /** A method to bind a pan and zoom behaviour to an svg element */
  applyZoomableBehaviour(svgElement, containerElement) {
    let svg, container, zoomed, zoom;

    svg = d3.select(svgElement);
    container = d3.select(containerElement);

    zoomed = () => {
      let transform = d3.event.transform;
      container.attr("transform", "translate(" + transform.x + "," + transform.y + ") scale(" + transform.k + ")");
    };

    zoom = d3.zoom().on("zoom", zoomed);
    svg.call(zoom).on("dblclick.zoom", null);
  }

  /** A method to bind a draggable behaviour to an svg element */
  applyDraggableBehaviour(element, node: Node, graph: SimpleGraph) {
    let d3element = d3.select(element);

    function started() {
      if (!d3.event.active) {
        graph.simulation.alphaTarget(0.3).restart();
      }

      d3.event.on("drag", dragged).on("end", ended);

      function dragged() {
        node.fx = d3.event.x;
        node.fy = d3.event.y;
        console.log(node.attr.get("name"))
      }

      function ended() {
        if (!d3.event.active) {
          graph.simulation.alphaTarget(0).alphaDecay(0.9);
        }
        node.x = d3.event.x;
        node.y = d3.event.y;
        // node.fx = null;
        // node.fy = null;
      }
    }

    d3element.call(d3.drag()
      .on("start", started));
  }

  /** The interactable graph we will simulate in this article
   * This method does not interact with the document, purely physical calculations with d3
   */
  getForceDirectedGraph(nodes: Node[], links: Link[], options: { width, height }) {
    return new SimpleGraph(nodes, links, options);
  }


}
