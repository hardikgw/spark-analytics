import {Injectable} from "@angular/core";

@Injectable()
export class Globals {
  public totalNodes: number = 0;
  public verticesLinkCount: Map<string, number>  = new Map();
}
