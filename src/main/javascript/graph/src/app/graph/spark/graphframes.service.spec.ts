import { TestBed, inject } from '@angular/core/testing';

import { GraphframesService } from './graphframes.service';

describe('GraphframesService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [GraphframesService]
    });
  });

  it('should be created', inject([GraphframesService], (service: GraphframesService) => {
    expect(service).toBeTruthy();
  }));
});
