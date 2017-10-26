import {Injectable} from "@angular/core";

@Injectable()
export class Globals {
  public totalNodes: number;
  public verticesLinkCount: Map<string, number>;
}
