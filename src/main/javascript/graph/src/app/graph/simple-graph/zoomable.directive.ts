import { Directive, Input, ElementRef } from '@angular/core';
import { SimpleGraphService} from "./simple-graph.service";

@Directive({
  selector: '[zoomableOf]'
})
export class ZoomableDirective {
  @Input('zoomableOf') containerElement: ElementRef;

  constructor(private service: SimpleGraphService, private _element: ElementRef) {}

  ngOnInit() {
    this.service.applyZoomableBehaviour(this.containerElement, this._element.nativeElement);
  }
}
