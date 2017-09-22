import {Component, Input, OnInit} from '@angular/core';
import {Link} from "../link";

@Component({
  selector: '[linkVisual]',
  templateUrl: './simple-graph-link-svg.component.html',
  styleUrls: ['./simple-graph-link-svg.component.css']
})
export class SimpleGraphLinkSvgComponent implements OnInit {

  @Input('linkVisual') link: Link;

  constructor() { }

  ngOnInit() {
  }

}
