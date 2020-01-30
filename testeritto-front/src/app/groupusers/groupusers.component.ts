import { Component, OnInit, QueryList, ViewChildren } from '@angular/core';
import { GroupService } from '../core/api/group.service';
import { Router, ActivatedRoute } from '@angular/router';
import { Test } from '../core/models/test.model';
import { Result } from '../core/models/result.model';
import { ResultService } from '../core/api/result.service';
import { MatDialog, MatTableDataSource, MatPaginator, MatSort, MatTabChangeEvent } from '@angular/material';
import { ConfirmDeleteComponent } from '../confirm-delete/confirm-delete.component';
import { User } from '../core/models/user.model';

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
  showTabPassedTests: Boolean = false;
  showTableTests: Boolean = false;
  showResults: Boolean = false;
  selectedIndex: number;
  color = 'primary';
  mode = 'indeterminate';
  value = 50;
  loading: Boolean = false;

  @ViewChildren(MatPaginator) paginator = new QueryList<MatPaginator>();
  @ViewChildren(MatSort) sort = new QueryList<MatSort>();
  usersDataSource = new MatTableDataSource<User>();
  testsDataSource = new MatTableDataSource<Test>();
  resultsDataSource = new MatTableDataSource<Result>();
  displayedUsersColumns: string[] = ['lastName','firstName','seeTests','kickOut']
  displayedTestsColumns: string[] = ['nameTest','seeResults']
  displayedResultsColumns: string[] = ['date', 'status', 'action'];
  constructor(private route: ActivatedRoute,
              private router: Router,
              private groupService: GroupService,
              public dialog: MatDialog,
              private resultService: ResultService) {
    route.params.subscribe(params=>this.groupId=params['groupId']);  
    if(this.groupService.users == undefined){      
      this.router.navigateByUrl('/group/'+this.groupId);
    }
   }

  ngOnInit() {
    this.groupService.getUsersInGroup(this.groupId).subscribe(data => {
      this.groupService.users = data;
      this.usersDataSource = new MatTableDataSource<User>(data);
      this.usersDataSource.paginator = this.paginator.toArray()[0];
      this.usersDataSource.sort = this.sort.toArray()[0];
      this.loading = true;
    });    
  }

  tabChanged(tabChangeEvent: MatTabChangeEvent) {
    this.selectedIndex = tabChangeEvent.index;
    if(this.selectedIndex == 0) {
      this.usersDataSource.paginator = this.paginator.toArray()[0];
      this.usersDataSource.sort = this.sort.toArray()[0];
    }
    else if(this.selectedIndex == 1) {
      this.testsDataSource.paginator = this.paginator.toArray()[1];
      this.testsDataSource.sort = this.sort.toArray()[1];
    }
    else {
      this.resultsDataSource.paginator = this.paginator.toArray()[2];
      this.resultsDataSource.sort = this.sort.toArray()[2];
    }
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
    this.showTabPassedTests = true;
    if(this.tests.size == 0) 
      this.showTableTests = false;
    else 
      this.showTableTests = true;
    var testsToDisplay = new Array();
    this.tests.forEach(test => testsToDisplay.push(test))
    this.testsDataSource = new MatTableDataSource<Test>(testsToDisplay);
    this.testsDataSource.paginator = this.paginator.toArray()[1];
      this.testsDataSource.sort = this.sort.toArray()[1];
    this.selectedIndex = 1;
    this.showResults = false;
  }

  seeResultsForTest(id: BigInteger) {
    this.selectedTest = id;
    var result = this.groupService.users.find(user => user.id = this.selectedUser).results.filter(element => element.testId == this.selectedTest);
    result.forEach(element => {
      if(this.userResults.find(elem => elem.status == element.status && elem.date == element.date) == undefined) {
        this.userResults.push(element);
      }
    });
    this.showResults = true;
    this.resultsDataSource = new MatTableDataSource<Result>(this.userResults);
    this.resultsDataSource.paginator = this.paginator.toArray()[2];
    this.resultsDataSource.sort = this.sort.toArray()[2];
    this.selectedIndex = 2;
  }

  showDetailsOnResult(date: Date) {
    var result = this.groupService.users.find(user => user.id = this.selectedUser).results.filter(element => element.testId == this.selectedTest
       && element.date == date && element.status == "PASSED");
    this.resultService.results = result;
    this.router.navigateByUrl('/group/'+this.groupId+'/results');
  }

  kickOutUser(id: BigInteger) {
    const dialogRef = this.dialog.open(ConfirmDeleteComponent, {
      data: {title: "KICK OUT FROM GROUP", 
             text: "Are You sure that you want to kick out " +  this.groupService.users.find(user => user.id == id).lastName 
                   +" "+ this.groupService.users.find(user => user.id == id).firstName + " from this group?" }
    });

    dialogRef.afterClosed().subscribe(result => {
      if(result) {
        this.groupService.exitFromGroup(id, this.groupId).subscribe();
        var index = this.groupService.users.findIndex(user => user.id == id);
        if(index != -1)
          this.groupService.users.splice(index, 1); 
      }
    });
  }

  goBackToGroup() {
    this.router.navigateByUrl('/group/'+this.groupId);
  }
}
