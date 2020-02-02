import { Component, OnInit, ViewChild, QueryList, ViewChildren } from '@angular/core';
import { GroupService } from '../core/api/group.service';
import { ActivatedRoute, Router } from '@angular/router';
import { TestService } from '../core/api/test.service';
import { MatPaginator, MatTableDataSource, MatSort, MatTabChangeEvent, MatDialog } from '@angular/material';
import { Remark } from '../core/models/remark.model';
import { CreateGroupFormComponent } from '../create-group-form/create-group-form.component';
import { Question } from '../core/models/question.model';
import { ConfirmDeleteComponent } from '../confirm-delete/confirm-delete.component';

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
  loading: Boolean = false;
  color = 'primary';
  mode = 'indeterminate';
  value = 50;
  constructor(private groupService: GroupService,
              private testService: TestService,
              private router: Router,
              public dialog: MatDialog,
              private route: ActivatedRoute) {
    route.params.subscribe(params=>this.groupId=params['groupId']);  
    if(this.groupService.remarks == undefined){      
      this.router.navigateByUrl('/group/'+this.groupId);
    }
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
        this.changeNotViewedDataSource();
        this.changeViewedDataSource();
        this.loading = true;
      }
    }));   
  }

  changeNotViewedDataSource(){
    this.NotViewedDataSource = new MatTableDataSource<Remark>(this.groupService.remarks.filter(remark => remark.viewed == false));
    this.updateSortAndPaginator(0, this.NotViewedDataSource);
  }

  changeViewedDataSource(){
    this.ViewedDataSource = new MatTableDataSource<Remark>(this.groupService.remarks.filter(remark => remark.viewed == true));
    this.updateSortAndPaginator(1, this.ViewedDataSource);
  }

  updateSortAndPaginator(index: number, dataSource: MatTableDataSource<Remark>) {
    dataSource.paginator = this.paginator.toArray()[index];
    dataSource.sort = this.sort.toArray()[index];
  }

  tabChanged(tabChangeEvent: MatTabChangeEvent) {
    this.selectedIndex = tabChangeEvent.index;
    if(this.selectedIndex == 0) {
      this.updateSortAndPaginator(0, this.NotViewedDataSource);
    }
    else {
      this.updateSortAndPaginator(1, this.ViewedDataSource);
    }
  }

  checkedAsViewed(id: BigInteger) {
      this.groupService.setViewedStatus(id).subscribe(() => {
        this.groupService.remarks.find(remark => remark.id == id).viewed = true;
        this.changeNotViewedDataSource();
        this.changeViewedDataSource();
      });      
  }

  changeQuestion(questionId: BigInteger, nameTest: String) {
    const dialogRef = this.dialog.open(CreateGroupFormComponent, {
      data: {action: "changeQuestion", 
             question: this.groupService.tests.find(test => test.nameTest == nameTest).questions.find(question => question.id == questionId)},
      width: "450px"
    });

    dialogRef.afterClosed().subscribe(result => {
      if(result) {
        this.groupService.tests.find(test => test.nameTest == nameTest).questions.find(
          question => question.id == result.id).textQuestion = result.textQuestion
        this.NotViewedDataSource.data.filter(remarks => remarks.questionId == questionId).forEach(
          remark => {
            remark.questionText = result.textQuestion;
          }
        )
      }
    })
  }

  goBackToGroup() {
    this.router.navigateByUrl('/group/'+this.groupId);
  }

  openQuestionText(remark: Remark) {
      if(remark.questLimit == 20) 
        remark.questLimit = remark.questionText.length;      
      else 
        remark.questLimit = 20;
  }

  openRemarkText(remark: Remark) {
    if(remark.remarkLimit == 20) 
      remark.remarkLimit = remark.text.length;      
    else 
      remark.remarkLimit = 20;
  }

  deleteRemark(id: BigInteger) {
    const dialogRef = this.dialog.open(ConfirmDeleteComponent, {
      data: {title: "DELETE REMARK", text: "Are You sure that you want to delete this remark?"}
    });

    dialogRef.afterClosed().subscribe(result => {
      if(result) {
        this.groupService.deleteRemark(id).subscribe();
        var index = this.ViewedDataSource.data.findIndex(remark => remark.id == id);
        if(index != -1) {
          this.groupService.remarks.splice(index,1);
          this.changeViewedDataSource();
        }    
      }
    })
    
  }
}
