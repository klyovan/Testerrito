import { Component, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { Result } from '../core/models/result.model';
import { ActivatedRoute, Router } from '@angular/router';
import { ResultService } from '../core/api/result.service';

@Component({
  selector: 'app-result',
  templateUrl: './result.component.html',
  styleUrls: ['./result.component.css']
})
export class ResultComponent implements OnInit {
  results: Array<Result>;
  status: String;
  date: Date;
  groupId: BigInteger;
  constructor(private route: ActivatedRoute,
              private router: Router,
              private resultService: ResultService) {
    if(this.resultService.results == undefined){
      route.params.subscribe(params=>this.groupId=params['groupId']);  
      this.router.navigateByUrl('/group/'+this.groupId);
    }
    
   }

  ngOnInit() {
    this.results = this.resultService.results;
    this.date = this.results.find(element => element.id!=undefined).date;
    this.status = this.results.find(element => element.id!=undefined).status;
  }

}
