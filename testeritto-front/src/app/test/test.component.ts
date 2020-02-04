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
import { MatTableDataSource } from '@angular/material';
import { ThrowStmt } from '@angular/compiler';

@Component({
  selector: 'app-test',
  templateUrl: './test.component.html',
  styleUrls: ['./test.component.css']
})
export class TestComponent implements OnInit {
    testId: BigInteger;
    groupId: BigInteger;
    creatorUserId: BigInteger;
    gradesCategory: Array<GradeCategory> = new Array<GradeCategory>();
    categories: Array<Category> = new Array<Category>();
    questions: Array<Question> = new Array<Question>();
    answers: Array<Answer> = new Array<Answer>();
    test: Test;
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
    categoryName = new FormControl(this.category_name, [Validators.required, Validators.maxLength(50)]);
    gradeCategoryName = new FormControl(this.gr_cat_name, [Validators.required, Validators.maxLength(150)]);
    gradeCategoryMinScore = new FormControl(this.gr_min_score, [Validators.required, Validators.maxLength(10), Validators.pattern('[0-9]*')]);
    gradeCategoryMaxScore = new FormControl(this.gr_max_score, [Validators.required, Validators.maxLength(10), Validators.pattern('[0-9]*')]);
    question_name = new FormControl(this.text_question, [Validators.required, Validators.maxLength(150)]);
    answers_name = new FormControl(this.text_answer, [Validators.required, Validators.maxLength(200)]);
    score = new FormControl(this.score_answer, [Validators.required, Validators.maxLength(10),Validators.pattern('[0-9]*')]);
    radioForm = new FormControl('', [Validators.required]);
    selectQuestion = new FormControl('', [Validators.required]);
    selectCategories = new FormControl('', [Validators.required]);
    displayedColumns: string[] = ['categories'];
    displayedColumnsGrCat: string[] = ['grade categories'];
    displayedColumnsQuest: string[] = ['questions'];
    displayedColumnsAnswer: string[] = ['answers'];
    consistTestDataSourse = new MatTableDataSource<Category>(this.categories); 
    consistGrCatDataSourse = new MatTableDataSource<GradeCategory>(this.gradesCategory); 
    consistQustionDataSourse = new MatTableDataSource<Question>(this.questions); 
    consistAnswerDataSourse = new MatTableDataSource<Answer>(this.answers); 
    selectedCategoryId : BigInteger;
    selectedQuestionId : BigInteger;
    grCategoryText = "Grade category is needed in order to draw conclusions on a specific category at the end.";
    succesfull = '';
    errorAddedGrCat = '';
    errorAddedCat ='';
    errorAddedQuest = '';
    errorAddedAnsw ='';


  constructor(private route: ActivatedRoute, private router: Router,private formBuilder: FormBuilder,
    private testService: TestService, private userService: UserService,private groupService: GroupService,private passTestService: PassTestService) { 
      this.route.params.subscribe((params) => {
        this.testId = params['testId'];
        this.userId = params['userId'];
        this.groupId = params['groupId'];
    });
  }

  ngOnInit() {
    this.groupService.getGroup(this.groupId).subscribe(group => this.userId = group.creatorUserId);
    this.testService.getTest(this.testId).subscribe(test => this.testId = test.id);
  }

 public createCategory(){
  this.category = new Category();
  this.category.nameCategory = this.categoryName.value;
  console.log(this.category)

  if(this.categories.find(elem => elem.nameCategory == this.category.nameCategory)== undefined){
    this.categories.push(this.category);
    this.consistTestDataSourse = new MatTableDataSource(this.categories);
    this.testService.createCategory(this.category).subscribe(id => {this.category.id = id;});
    this.succesfull = "Category added";
}
  else{this.errorAddedCat = "Category with this name already exist!";
    console.log("name already exist!")
  }
  console.log(this.answers)
  console.log(this.categories)
  console.log(this.consistTestDataSourse)
  this.categoryName.reset();
 }

 public createGradeCategory(){
  this.gradeCategory = new GradeCategory();
  this.gradeCategory.meaning = this.gradeCategoryName.value;
  this.gradeCategory.minScore = this.gradeCategoryMinScore.value;
  this.gradeCategory.maxScore = this.gradeCategoryMaxScore.value;
  this.gradeCategory.testId = this.testId;
  this.gradeCategory.categoryId = this.selectedCategoryId;
  console.log(this.gradeCategory)
  console.log(this.selectedCategoryId)
  if(this.gradesCategory.find(elem => elem.meaning == this.gradeCategory.meaning)== undefined){
  this.gradesCategory.push(this.gradeCategory);
  this.consistGrCatDataSourse = new MatTableDataSource(this.gradesCategory);
  this.testService.createGradeCategory(this.gradeCategory).subscribe();
  this.succesfull = "Grade category added";
}
  else{
    console.log("name already exist!")
    this.errorAddedGrCat ="Grade Categry with this meaning already exist!";
  }
  console.log(this.consistGrCatDataSourse)
  this.gradeCategoryName.reset();
  this.gradeCategoryMinScore.reset();
  this.gradeCategoryMaxScore.reset();
}

public createQuestions(){
  this.question = new Question();
  this.question.testId = this.testId;
  this.question.textQuestion = this.question_name.value;
  this.question.typeQuestion = this.typeQuestion;
  this.question.categoryId = this.selectedCategoryId;

  if(this.questions.find(elem => elem.textQuestion == this.question.textQuestion)== undefined){
    this.questions.push(this.question);
    this.consistQustionDataSourse = new MatTableDataSource(this.questions);
    this.testService.createQuestion(this.question).subscribe(id => {this.question.id = id;});
    this.succesfull = "Question added";
  }
    else{
      this.errorAddedQuest = "Question with this names already exist!";
      console.log("name already exist!")}
  
  console.log(this.question)
  console.log(this.selectedCategoryId)
  console.log(this.consistQustionDataSourse)
  this.question_name.reset();
 }

 public createAnswers(){
  this.answer = new Answer();
  this.answer.score = this.score.value;
  this.answer.textAnswer = this.answers_name.value;
  this.answer.questionId = this.selectedQuestionId;
  console.log(this.answer)
  console.log(this.selectedQuestionId)
  if(this.answers.find(elem => elem.textAnswer == this.answer.textAnswer)== undefined){
      this.answers.push(this.answer);
      this.consistAnswerDataSourse = new MatTableDataSource(this.answers);
      this.testService.createAnswer(this.answer).subscribe();
      this.succesfull = "Answer added ";
}
    else{
      this.errorAddedAnsw = "Answer with this name already exist!";
      console.log("name already exist!")
    console.log(this.answers)}
  this.answers_name.reset();
  this.score.reset();
 }

 navigateFinish(){
  this.router.navigateByUrl('test/'+ this.groupId+'/update/' + this.testId);
 }
}