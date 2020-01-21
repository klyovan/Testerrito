import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-error-page',
  templateUrl: './error-page.component.html',
  styleUrls: ['./error-page.component.css']
})
export class ErrorPageComponent implements OnInit {

  status: any;

  constructor(private route: ActivatedRoute,) {
    route.params.subscribe(param=> this.status =(param["status"]));  
  }

  ngOnInit() {
  }

}
