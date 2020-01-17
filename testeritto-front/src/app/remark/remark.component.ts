import { Component, OnInit, ViewChild, QueryList, ViewChildren } from '@angular/core';
import { GroupService } from '../core/api/group.service';
import { ActivatedRoute, Router } from '@angular/router';
import { TestService } from '../core/api/test.service';
import { MatPaginator, MatTableDataSource, MatSort, MatTabChangeEvent } from '@angular/material';
import { Remark } from '../core/models/remark.model';

@Component({
  selector: 'app-remark',
  templateUrl: './remark.component.html',
  styleUrls: ['./remark.component.css']
})
export class RemarkComponent implements OnInit {
  @ViewChildren(MatPaginator) paginator = new QueryList<MatPaginator>();
  @ViewChildren(MatSort) sort = new QueryList<MatSort>();
  displayedNotViewedColumns: string[] = ['testName', 'questionText', 'text', 'update', 'seen'];
  displayedViewedColumns: string[] = ['testName', 'questionText', 'text', 'delete'];
  groupId: BigInteger;
  NotViewedDataSource = new MatTableDataSource<Remark>();
  ViewedDataSource = new MatTableDataSource<Remark>();
  selectedIndex: Number = 0;
  constructor(private groupService: GroupService,
              private testService: TestService,
              private router: Router,
              private route: ActivatedRoute) {
    route.params.subscribe(params=>this.groupId=params['groupId']);  
  }

  ngOnInit() {
    var counter = 0;    
    this.groupService.tests.forEach(test => this.testService.getTestQuestions(test.id).subscribe(questions =>{
      test.questions = questions;
      this.groupService.remarks.forEach(remark => {        
        if(test.questions!= null){
          if(test.questions.find(question => {
            if(question.id == remark.questionId) {
              remark.questionText = question.textQuestion;
              return question;
            }
          }) != undefined ) {
            remark.testName = test.nameTest;
            return test;
          }    
        }        
      })     
      counter++;
      if(counter == this.groupService.tests.length) {
        this.NotViewedDataSource = new MatTableDataSource<Remark>(this.groupService.remarks.filter(remark => remark.viewed == false));
        this.NotViewedDataSource.paginator = this.paginator.toArray()[0];
        this.NotViewedDataSource.sort = this.sort.toArray()[0];
        this.ViewedDataSource = new MatTableDataSource<Remark>(this.groupService.remarks.filter(remark => remark.viewed == true));
        this.ViewedDataSource.paginator = this.paginator.toArray()[1];
        this.ViewedDataSource.sort = this.sort.toArray()[1];
      }
    }));   
  }

  tabChanged(tabChangeEvent: MatTabChangeEvent) {
    this.selectedIndex = tabChangeEvent.index;
    if(this.selectedIndex == 0) {
      this.NotViewedDataSource.paginator = this.paginator.toArray()[0];
      this.NotViewedDataSource.sort = this.sort.toArray()[0];
    }
    else {
      this.ViewedDataSource.paginator = this.paginator.toArray()[1];
      this.ViewedDataSource.sort = this.sort.toArray()[1];
    }
}

  checkedAsViewed(id: BigInteger) {
      this.groupService.setViewedStatus(id).subscribe(() => {
        this.groupService.remarks.find(remark => remark.id == id).viewed = true;
        this.NotViewedDataSource = new MatTableDataSource<Remark>(this.groupService.remarks.filter(remark => remark.viewed == false));
        this.NotViewedDataSource.paginator = this.paginator.toArray()[0];
        this.NotViewedDataSource.sort = this.sort.toArray()[0];
        this.ViewedDataSource = new MatTableDataSource<Remark>(this.groupService.remarks.filter(remark => remark.viewed == true));
        this.ViewedDataSource.paginator = this.paginator.toArray()[1];
        this.ViewedDataSource.sort = this.sort.toArray()[1];
      });      
  }

  goBackToGroup() {
    this.router.navigateByUrl('/group/'+this.groupId);
  }
}
