import { Component, OnInit } from '@angular/core';
import { GroupService } from '../core/api/group.service';
import { Router, ActivatedRoute } from '@angular/router';
import { Test } from '../core/models/test.model';
import { Result } from '../core/models/result.model';
import { ResultService } from '../core/api/result.service';

@Component({
  selector: 'app-groupusers',
  templateUrl: './groupusers.component.html',
  styleUrls: ['./groupusers.component.css']
})
export class GroupusersComponent implements OnInit {
  groupId: BigInteger;
  tests: Set<Test> = new Set();
  userResults: Array<Result> = new Array();
  selectedUser: BigInteger;
  selectedTest: BigInteger;
  showPassedTests: Boolean = false;
  showResults: Boolean = false;
  constructor(private route: ActivatedRoute,
              private router: Router,
              private groupService: GroupService,
              private resultService: ResultService) {
    route.params.subscribe(params=>this.groupId=params['groupId']);  
    if(this.groupService.users == undefined){      
      this.router.navigateByUrl('/group/'+this.groupId);
    }
   }

  ngOnInit() {
  }

  seeAllPassedTestsByUser(id: BigInteger) {
    this.selectedUser = id;
    if(this.groupService.users.find(user => user.id == id).results == undefined)
      this.groupService.getUserResultsForTest(id).subscribe(data => {
        this.groupService.users.find(user => user.id == id).results = data;
        this.sortTests();
      });   
    else {
      this.sortTests();
    }
  }

  sortTests() {
    this.tests = new Set();
    this.groupService.users.find(user=>user.id == this.selectedUser).results.forEach(result => {
      this.tests.add(this.groupService.tests.find(test => test.id == result.testId))
    })
    this.showPassedTests = true;
  }

  seeResultsForTest(id: BigInteger) {
    this.selectedTest = id;//отсортировать, сделать кнопку по одному резалту
    var result = this.groupService.users.find(user => user.id = this.selectedUser).results.filter(element => element.testId == this.selectedTest);
    result.forEach(element => {
      if(this.userResults.find(elem => elem.status == element.status && elem.date == element.date) == undefined) {
        this.userResults.push(element);
      }
    });
    this.showResults = true;
  }

  showDetailsOnResult(date: Date) {
    var result = this.groupService.users.find(user => user.id = this.selectedUser).results.filter(element => element.testId == this.selectedTest
       && element.date == date && element.status == "PASSED");
    this.resultService.results = result;
    this.router.navigateByUrl('/group/'+this.groupId+'/results');
  }
}
