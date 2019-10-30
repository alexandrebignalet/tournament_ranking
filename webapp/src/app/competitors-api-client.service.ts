import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {Observable} from "rxjs";

export class Competitor {
  constructor(public pseudo: String, public points: Number) {}
}

@Injectable({
  providedIn: 'root'
})
export class CompetitorsApiClientService {
  private competitorsUrl = 'tournament/competitors';

  constructor(private http: HttpClient) { }

  getCompetitors (): Observable<Competitor[]> {
    return this.http.get<Competitor[]>(this.competitorsUrl)
  }
}
