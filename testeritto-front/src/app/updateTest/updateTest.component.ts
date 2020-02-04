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
    category_id: BigInteger;
    grade_category_id : BigInteger;
    question_id : BigInteger;
    answer_id:BigInteger;
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
    name = new FormControl('', [Validators.required, Validators.maxLength(30)]);
    loading: Boolean = false;
    selectedIndex: Number = 0;
    categorySets: Set<BigInteger> = new Set();
    createdCategoryDataSourse = new MatTableDataSource<Category>(this.categories); 
    createdGradeCategoryDataSourse = new MatTableDataSource<GradeCategory>(this.test.gradesCategory);  
    createdQuestionsDataSourse = new MatTableDataSource<Question>(this.test.questions);  
    createdAnswersDataSourse = new MatTableDataSource<Answer>(this.answers);  
    grCategoryText = "Grade category is needed in order to draw conclusions on a specific category at the end.";

    categoryName = new FormControl('', [Validators.required, Validators.maxLength(50)]);
    gradeCategoryName = new FormControl('', [Validators.required, Validators.maxLength(150)]);
    gradeCategoryMinScore = new FormControl('', [Validators.required, Validators.maxLength(10), Validators.pattern('[0-9]*')]);
    gradeCategoryMaxScore = new FormControl('', [Validators.required, Validators.maxLength(10), Validators.pattern('[0-9]*')]);
    question_name = new FormControl('', [Validators.required, Validators.maxLength(150)]);
    answers_name = new FormControl('', [Validators.required, Validators.maxLength(200)]);
    score = new FormControl('', [Validators.required, Validators.maxLength(10),Validators.pattern('[0-9]*')]);
    radioForm = new FormControl('', [Validators.required]);
    selectQuestion = new FormControl('', [Validators.required]);
    selectCategories = new FormControl('', [Validators.required]);
    selectedCategoryId : BigInteger;
    selectedQuestionId : BigInteger;

    displayedColumnsCategories: string[] = ['categories','update', 'delete'];
    displayedColumnsGradeCategories: string[] = ['categories','grade categories','update', 'delete'];
    displayedColumnsQuestions: string[] = ['categories','questions','update', 'delete'];
    displayedColumnsAnswers: string[] = ['questions','answers','update', 'delete'];
    isGradeEmpty: Boolean = false;
    isQuestionEmpty: Boolean = false;
    isAnswerEmpty: Boolean = false;

    windowUpdateCat = false;
    windowUpdateGrCat = false;
    windowUpdateQuestion = false;
    windowUpdateAnswer = false;

    constructor(private route: ActivatedRoute, private router: Router,private formBuilder: FormBuilder,
      private testService: TestService, private userService: UserService,private groupService: GroupService,private passTestService: PassTestService) { 
        this.route.params.subscribe((params) => {
          this.testId = params['testId'];
          this.userId = params['userId'];
          this.groupId = params['groupId'];
      });
    }

  ngOnInit() {
    this.testService.getTest(this.testId).subscribe(test => this.testId = test.id);
    this.testService.getTest(this.testId)
      .subscribe((test: Test) => {
      this.test = test;
      this.name = new FormControl(this.test.nameTest, [Validators.required, Validators.maxLength(30)]);
      console.log(this.test)
      this.test.gradesCategory.forEach(gradeCat=>{
        if(!this.categorySets.has(gradeCat.categoryId)){
          this.categorySets.add(gradeCat.categoryId);
        }
      })
      this.test.questions.forEach(data=>{
        data.answers.forEach(answer=>{
          this.answers.push(answer);
        })
      })
      this.categorySets.forEach(elements=>{
        this.testService.getCategoryById(elements).subscribe(data=>{
          this.categories.push(data);
          this.test.gradesCategory.filter(elem=>elem.categoryId == elements).forEach(elem2=>{
            elem2.categoryName = data.nameCategory;
          })
          this.test.questions.filter(elem=>elem.categoryId == elements).forEach(elem2=>{
            elem2.categoryName = data.nameCategory;
          })
          console.log(this.test.gradesCategory)
          this.test.questions.forEach(data=>{
            data.answers.filter(elem=> elem.questionId == data.id).forEach(elem2=>{
              elem2.questionText = data.textQuestion;
            })
          })
          this.createdCategoryDataSourse = new MatTableDataSource(this.categories);
        })
      })
      
      this.createdGradeCategoryDataSourse = new MatTableDataSource(this.test.gradesCategory);
      this.createdQuestionsDataSourse = new MatTableDataSource(this.test.questions);
      this.createdAnswersDataSourse = new MatTableDataSource<Answer>(this.answers);  
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
    var index = this.categories.findIndex(elem=>elem.id == this.category_id);
    this.categories[index].nameCategory = this.categoryName.value;
    this.testService.updateCategory(this.categories[index]).subscribe(data=>{
      this.categories.find(elem=>elem.id == this.category_id).nameCategory = data.nameCategory;
    })
    this.windowUpdateCat = false;
    this.createdCategoryDataSourse = new MatTableDataSource(this.categories);
  }
  updateGradeCategory(){
    var index = this.test.gradesCategory.findIndex(elem=>elem.id == this.grade_category_id);
    this.test.gradesCategory[index].meaning = this.gradeCategoryName.value;
    this.test.gradesCategory[index].minScore = this.gradeCategoryMinScore.value;
    this.test.gradesCategory[index].maxScore = this.gradeCategoryMaxScore.value;
    this.testService.updateGradeCategory(this.test.gradesCategory[index]).subscribe(data => {
      console.log(this.test.gradesCategory.find(elem=>elem.id == this.grade_category_id).meaning)
      console.log(data)
      this.test.gradesCategory.find(elem=>elem.id == this.grade_category_id).meaning = data.meaning;
      console.log(this.test.gradesCategory.find(elem=>elem.id == this.grade_category_id).meaning)
      this.test.gradesCategory.find(elem=>elem.id == this.grade_category_id).minScore = data.minScore;
      this.test.gradesCategory.find(elem=>elem.id == this.grade_category_id).maxScore = data.maxScore;});
    this.windowUpdateGrCat = false;
    this.createdGradeCategoryDataSourse = new MatTableDataSource(this.test.gradesCategory);
  }
  updateQuestion(){
    var index = this.test.questions.findIndex(elem=>elem.id == this.question_id);
    this.test.questions[index].textQuestion = this.question_name.value;
    this.testService.updateQuestion(this.test.questions[index]).subscribe(data => this.test.questions
      .find(elem=>elem.id == this.question_id).textQuestion = data.textQuestion);
    this.windowUpdateQuestion = false;
    this.createdQuestionsDataSourse = new MatTableDataSource(this.test.questions);
  }
  updateAnswer(){
    var index = this.answers.findIndex(elem=>elem.id == this.answer_id);
    this.answers[index].textAnswer = this.answers_name.value;
    this.answers[index].score = this.score.value;
    this.testService.updateAnswer(this.answers[index]).subscribe(data => {this.answers
      .find(elem=>elem.id == this.answer_id).textAnswer = data.textAnswer;
      this.answers
      .find(elem=>elem.id == this.answer_id).score = data.score;})
      this.windowUpdateAnswer = false;
      this.createdAnswersDataSourse = new MatTableDataSource(this.answers);
  }

  deleteCategory(id: BigInteger){
    this.testService.deleteCategory(id).subscribe(()=>{
      var index = this.categories.findIndex(elem=>elem.id == id);
      this.categories.splice(index,1);
      this.createdCategoryDataSourse = new MatTableDataSource(this.categories);
  });
  }
  deleteGradeCategory(id: BigInteger){
    this.testService.deleteGradeCategory(id).subscribe(()=>{
      var index = this.test.gradesCategory.findIndex(elem=>elem.id == id);
      this.test.gradesCategory.splice(index,1);
      this.createdGradeCategoryDataSourse = new MatTableDataSource(this.test.gradesCategory);
  });
  }
  deleteQuestion(id: BigInteger){
    this.testService.deleteQuestion(id).subscribe(()=>{
      var index = this.test.questions.findIndex(elem=>elem.id == id);
      this.test.questions.splice(index,1);
      this.createdQuestionsDataSourse = new MatTableDataSource(this.test.questions);
  });
  }
  deleteAnswer(id: BigInteger){
    this.testService.deleteAnswer(id).subscribe(()=>{
      var index = this.answers.findIndex(elem=>elem.id == id);
      this.answers.splice(index,1);
      this.createdAnswersDataSourse = new MatTableDataSource(this.answers);
  });
    
  }



  openWindowForUpdateCategory(data:Category){
    this.windowUpdateCat = true;
    this.categoryName = new FormControl(data.nameCategory, [Validators.required, Validators.maxLength(50)]);
    this.category_id = data.id;
  }
  openWindowForUpdateGradeCategory(data:GradeCategory){
    this.windowUpdateGrCat = true;
    this.gradeCategoryName = new FormControl(data.meaning, [Validators.required, Validators.maxLength(150)]);
    this.gradeCategoryMinScore = new FormControl(data.minScore, [Validators.required, Validators.maxLength(10), Validators.pattern('[0-9]*')]);
    this.gradeCategoryMaxScore = new FormControl(data.maxScore, [Validators.required, Validators.maxLength(10), Validators.pattern('[0-9]*')]);
    this.grade_category_id = data.id;
  }
  openWindowForUpdateQuestions(data:Question){
    this.windowUpdateQuestion = true;
    this.question_name = new FormControl(data.textQuestion, [Validators.required, Validators.maxLength(150)]);
    this.question_id = data.id;
  }
  openWindowForUpdateAnswers(data:Answer){
    this.windowUpdateAnswer = true;
    this.answers_name = new FormControl(data.textAnswer, [Validators.required, Validators.maxLength(200)]);
    this.score = new FormControl(data.score, [Validators.required, Validators.maxLength(10),Validators.pattern('[0-9]*')]);
    this.answer_id = data.id;
  }
  navigateFinish(){
    this.router.navigateByUrl('group/'+ this.groupId);
   }
   navigeToCreate(){
    this.router.navigateByUrl('test/'+ this.groupId+'/create/' + this.testId);
   }
 

}