import { Directive, Input, ElementRef } from '@angular/core';
import { SimpleGraphService} from "./simple-graph.service";

import { Node } from './node';
import { SimpleGraph } from './simple-graph'

@Directive({
  selector: '[draggableNode]'
})
export class DraggableDirective {
  @Input('draggableNode') node: Node;
  @Input('draggableInGraph') graph: SimpleGraph;

  constructor(private service: SimpleGraphService, private _element: ElementRef) { }

  ngOnInit() {
    this.service.applyDraggableBehaviour(this._element.nativeElement, this.node, this.graph);
    this.service.applyDoubleClickableBehaviour(this._element.nativeElement, this.node, this.graph);
  }
}
