import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from "rxjs/Observable";

@Injectable()
export class GraphframesService {

  constructor(private http : HttpClient) {

  }
  getData(): Observable<any> {
    return this.http.get<any>('http://localhost:8421/graphframe/a')
  }
  getConnectedGraph(nodeId:String): Observable<any> {
    return this.http.get<any>('http://localhost:8421/graphframe/connected?nodeId=' + nodeId )
  }

}
