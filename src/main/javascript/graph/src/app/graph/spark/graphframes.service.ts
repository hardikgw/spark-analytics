import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from "rxjs/Observable";

@Injectable()
export class GraphframesService {

  constructor(private http : HttpClient) {

  }
  nodes(): Observable<any> {
    return this.http.get('/gf/nodes');
  }

}
