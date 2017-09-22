import {Component, Input, OnInit} from '@angular/core';
import { Node } from "../node";

@Component({
  selector: '[nodeVisual]',
  templateUrl: './simple-graph-node-svg.component.html',
  styleUrls: ['./simple-graph-node-svg.component.css']
})
export class SimpleGraphNodeSvgComponent implements OnInit {
  @Input('nodeVisual') node: Node;

  constructor() { }

  ngOnInit() {
  }

}
