import { Component, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { Result } from '../core/models/result.model';
import { ActivatedRoute } from '@angular/router';
import { ResultService } from '../core/api/result.service';

@Component({
  selector: 'app-result',
  templateUrl: './result.component.html',
  styleUrls: ['./result.component.css']
})
export class ResultComponent implements OnInit {
  result: Result;
  private subscription: Subscription;
  constructor(private route: ActivatedRoute,
              private resultService: ResultService) {
    this.result = new Result();
    this.subscription = route.params.subscribe(params=>this.result.id=params['resultId']);  
   }

  ngOnInit() {
    this.resultService.getResult(this.result.id).subscribe((result: Result) =>{
      this.result = result;
    })
  }

}
