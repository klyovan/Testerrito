import { Component, OnInit, QueryList, ViewChildren } from '@angular/core';
import { Group } from '../core/models/group.model';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../core/api/user.service';
import { User } from '../core/models/user.model';
import { GroupService } from '../core/api/group.service';
import { Result } from '../core/models/result.model';
import { ResultService } from '../core/api/result.service';
import { DatePipe } from '@angular/common';
import { PassTestService } from '../core/api/pass-test.service';
import { Test } from '../core/models/test.model';
import { TestService } from '../core/api/test.service';
import { MatDialog, MatTableDataSource, MatPaginator, MatSort } from '@angular/material';
import { CreateGroupFormComponent } from '../create-group-form/create-group-form.component';
import { ConfirmDeleteComponent } from '../confirm-delete/confirm-delete.component';

@Component({
  selector: 'app-group',
  templateUrl: './group.component.html',
  styleUrls: ['./group.component.css']
})
export class GroupComponent implements OnInit {
  group: Group = new Group();
  user: User;
  results: Array<Result> = new Array();
  finalResult: Map<Date,String>;
  loading: Boolean = false;
  testsLoaded: Boolean = false;
  showResult: Boolean = false;
  infoIfNoResult: String;
  selectedTest: BigInteger = null;
  test: Test;
  @ViewChildren(MatPaginator) paginator = new QueryList<MatPaginator>();
  @ViewChildren(MatSort) sort = new QueryList<MatSort>();
  testsDataSource = new MatTableDataSource<Test>();
  resultsDataSource = new MatTableDataSource<Result>();
  displayedTestsColumns: string[] = ['nameTest', 'passTest', 'seeResults', 'update', 'delete'];
  displayedResultsColumns: string[] = ['date', 'status', 'action'];
  color = 'primary';
  mode = 'indeterminate';
  value = 50;
  constructor(private route: ActivatedRoute,
              private userService: UserService,
              private groupService: GroupService,
              private resultService: ResultService,
              private passTestService: PassTestService,
              private testService: TestService,
              private router: Router,
              public dialog: MatDialog,
              private datePipe: DatePipe) {
    route.params.subscribe(params=>this.group.id=params['groupId']);
   }

  ngOnInit() {
    var check;
    this.userService.getUser().subscribe((user: User) => {
      this.user = user;
      check = user.groups.find(element => element.id == this.group.id);
      if(check != undefined) {
        this.groupService.getGroup(this.group.id).subscribe((group: Group) => {
          this.group = group;
          this.loading = true;
          this.changeTestsDataSourse();
          this.testsLoaded = true;
          this.groupService.tests = this.group.tests;
        });
        this.groupService.getUserResultsForTest(this.user.id).subscribe(data => {
          this.user.results = data;
        });
      }
      else
        this.router.navigate(['/group']);
    });
  }

  changeTestsDataSourse() {
    this.testsDataSource = new MatTableDataSource<Test>(this.group.tests);
    this.testsDataSource.paginator = this.paginator.toArray()[0];
    this.testsDataSource.sort = this.sort.toArray()[0];
  }

  changeResultsDataSourse() {
    this.resultsDataSource = new MatTableDataSource<Result>(this.results);
    this.resultsDataSource.sort = this.sort.toArray()[1];
    this.resultsDataSource.paginator = this.paginator.toArray()[1];
  }

  showResultsOnTest(id: BigInteger) {
    this.selectedTest = id;
    this.showResult = false;
    this.results = new Array();
    var result = this.user.results.filter(element => element.testId == this.selectedTest)
    var i = 0;
    result.forEach(element => {
      if(this.results != undefined && this.results.find(elem => elem.status == element.status && elem.date == element.date) == undefined) {
        this.results.push(element);
      }
      i++;
    })
    if(result.length == i && result.length!=0) {
      this.changeResultsDataSourse();
      this.showResult = true;
    }
    else this.showResult = false;
  }

  inviteMembers() {
    const dialogRef = this.dialog.open(CreateGroupFormComponent, {
      data: {action: "invite",
             link:  window.location.host + '/invite/' + this.group.link  },
      width: "450px"
    });
  }

  passTest(id: BigInteger) {
    var nowDate = this.datePipe.transform(new Date(),'yyyy-MM-dd');
    var checkResult = this.user.results.find( element => element.date.toString() == nowDate && element.testId == id )
    if(checkResult == undefined)
      this.router.navigateByUrl('/pass-test/' + this.user.id + '/test/' + id);
    else alert("You already passed test today. Come back tomorrow :)");
  }

  finishTest(testId: BigInteger) {
    this.passTestService.getFinishTest(this.user.id, testId).subscribe((test: Test) => {
        this.test = test;
        this.passTestService.notPassedTest = test;
        console.log('em');
        console.log(test);
        this.router.navigate(['/pass-test', this.user.id, 'test', this.test.id]);
    });

  }

  showDetailsOnResult(date: Date) {
    this.results = this.user.results.filter(element => element.testId == this.selectedTest
      && element.date == date && element.status == "PASSED")
    this.resultService.results = this.results;
    this.router.navigateByUrl('/group/'+this.group.id+'/results');
  }

  showUsersInGroup() {
    this.groupService.getUsersInGroup(this.group.id).subscribe(data => {
      this.groupService.users = data//.filter(user => user.id != this.group.creatorUserId);
      this.router.navigateByUrl('/group/'+this.group.id+'/users');
    });
  }

  showAllRemarks() {
    this.groupService.getAllRemarks(this.group.id).subscribe(data => {
      this.groupService.remarks = data;
      this.router.navigateByUrl('/group/'+this.group.id+'/remarks');
    })
  }

  deleteTest(id: BigInteger) {
    const dialogRef = this.dialog.open(ConfirmDeleteComponent, {
      data: {title: "DELETE TEST", text: "Are You sure that you want to delete this test?" }
    });

    dialogRef.afterClosed().subscribe(result => {
      if(result) {
        this.testService.deleteTest(id).subscribe();
        var index = this.group.tests.findIndex(test => test.id == id);
        if(index != -1) {
          this.group.tests.splice(index, 1);
          this.changeTestsDataSourse();
        }
      }
    });
  }
}
