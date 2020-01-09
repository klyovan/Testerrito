import { Component, OnInit } from '@angular/core';
import { GroupService } from '../core/api/group.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-remark',
  templateUrl: './remark.component.html',
  styleUrls: ['./remark.component.css']
})
export class RemarkComponent implements OnInit {
  groupId: BigInteger;
  constructor(private groupService: GroupService,
              private route: ActivatedRoute) {
    route.params.subscribe(params=>this.groupId=params['groupId']);  
  }

  ngOnInit() {
    console.log(this.groupService.remarks);
    console.log(this.groupService.tests)

    
  }

}
