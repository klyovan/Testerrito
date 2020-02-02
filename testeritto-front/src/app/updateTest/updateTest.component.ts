import { Component, OnInit } from '@angular/core';
import { TestService } from '../core/api/test.service';
import { Test } from '../core/models/test.model';
import { Question } from '../core/models/question.model';
import { Answer } from '../core/models/answer.model';
import { GradeCategory } from '../core/models/gradecategory.model';
import { User } from '../core/models/user.model';
import { Router, ActivatedRoute } from '@angular/router';
import { UserService } from '../core/api/user.service';
import { GroupService } from '../core/api/group.service';
import { Group } from '../core/models/group.model';
import { PassTestService } from '../core/api/pass-test.service';
import { FormBuilder,FormGroup, Validators, FormControl } from '@angular/forms';
import { Category } from '../core/models/category.model';
import { MatTabChangeEvent, MatTableDataSource } from '@angular/material';

@Component({
  selector: 'app-updateTest',
  templateUrl: './updateTest.component.html',
  styleUrls: ['./updateTest.component.css']
})
export class UpdateTestComponent implements OnInit {
    testId: BigInteger;
    groupId: BigInteger;
    creatorUserId: BigInteger;
    gradesCategory: Array<GradeCategory> = new Array<GradeCategory>();
    categories: Array<Category> = new Array<Category>();
    experts: Array<User>;
    questions: Array<Question> = new Array<Question>();
    answers: Array<Answer> = new Array<Answer>();
    test = new Test();
    userId: BigInteger;
    user: User;
    group: any;
    createTestForm: FormGroup;
    isSubmited: Boolean = false;
    error: string;
    nameTest: String;
    gradeCategory : GradeCategory;
    category:Category;
    category_name: string;
    gr_cat_name : string;
    gr_min_score: number;
    gr_max_score: number;
    question: Question;
    text_question: string;
    typeQuestion: string;
    categoryId: BigInteger;
    answer:Answer;
    text_answer: string;
    score_answer:number;
    questionId: BigInteger;
    buff:any;
    name = new FormControl(this.test.nameTest, [Validators.required, Validators.maxLength(30)]);
    loading: Boolean = false;
    selectedIndex: Number = 0;
    createdGradeCategoryDataSourse = new MatTableDataSource<GradeCategory>();  
    createdQuestionsDataSourse = new MatTableDataSource<Question>();  
    createdAnswersDataSourse = new MatTableDataSource<Answer>();  
    isGradeEmpty: Boolean = false;
    isQuestionEmpty: Boolean = false;
    isAnswerEmpty: Boolean = false;

  constructor(private route: ActivatedRoute, private router: Router,private formBuilder: FormBuilder,
    private testService: TestService, private userService: UserService,private groupService: GroupService,private passTestService: PassTestService) { 
      this.route.params.subscribe((params) => {
        this.testId = params['testId'];
    });
  }

  ngOnInit() {
    this.testService.getTest(this.testId).subscribe(test => this.testId = test.id);
    this.testService.getTest(this.testId)
      .subscribe((test: Test) => {
      this.test.id = test.id;
      this.test.nameTest = test.nameTest;
      this.test.creatorUserId = test.creatorUserId;
      this.test.groupId = test.groupId;
      this.test.gradesCategory = test.gradesCategory;
      this.test.questions = test.questions;
      console.log(this.test)
      }); 
  }

  tabChanged(tabChangeEvent: MatTabChangeEvent) {
    this.selectedIndex = tabChangeEvent.index;
  }
  updateTest(){
    this.test.nameTest = this.name.value;
    this.testService.updateTest(this.test).subscribe(data => this.test = data);
    console.log(this.test)
  }
  updateCategory(){
    this.test.nameTest = this.name.value;
    this.testService.updateTest(this.test).subscribe(data => this.test = data);
    console.log(this.test)
  }
  updategradeCategory(){
    this.test.nameTest = this.name.value;
    this.testService.updateTest(this.test).subscribe(data => this.test = data);
    console.log(this.test)
  }
  updateQuestion(){
    this.test.nameTest = this.name.value;
    this.testService.updateTest(this.test).subscribe(data => this.test = data);
    console.log(this.test)
  }
  updateAnswer(){
    this.test.nameTest = this.name.value;
    this.testService.updateTest(this.test).subscribe(data => this.test = data);
    console.log(this.test)
  }
}