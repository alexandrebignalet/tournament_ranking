import { Component } from '@angular/core';
import {Competitor, CompetitorsApiClientService} from "./competitors-api-client.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Tournament Ranking';
  private competitors: Competitor[] = [];

  constructor(private competitorsApiClient: CompetitorsApiClientService) {}

  ngOnInit() {
    this.getCompetitors()
  }

  getCompetitors(): void {
    this.competitorsApiClient.getCompetitors()
      .subscribe(competitors => this.competitors = competitors);
  }
}
