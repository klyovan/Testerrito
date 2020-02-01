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

@Component({
  selector: 'app-create-test',
  templateUrl: './create-test.component.html',
  styleUrls: ['./create-test.component.css']
})
export class CreateTestComponent implements OnInit {

    id: BigInteger;
    groupId: BigInteger;
    creatorUserId: BigInteger;
    gradesCategory: Array<GradeCategory>;
    experts: Array<User>;
    questions: Array<Question>;
    test: Test = new Test();
    userId: BigInteger;
    user: User;
    group: any;
    createTestForm: FormGroup;
    isSubmited: Boolean = false;
    error: string;
    nameTest: String;
    gradeCategory : GradeCategory;
    category:Category;
    category_name: String;
    gr_cat_name : String;
    gr_min_score: Int16Array;
    gr_max_score: Int16Array;
    name = new FormControl(this.test.nameTest, [Validators.required, Validators.maxLength(30)]);
    
    
  
    

  constructor(private route: ActivatedRoute, private router: Router,private formBuilder: FormBuilder,
    private testService: TestService, private userService: UserService,private groupService: GroupService,private passTestService: PassTestService) { 
      this.route.params.subscribe((params) => {
        this.id = params['id'];
        this.userId = params['userId'];
        this.groupId = params['groupId'];
    });
  }

  ngOnInit() {
    this.groupService.getGroup(this.groupId).subscribe(group => this.userId = group.creatorUserId);
  }

  public createTest(){
    console.log(this.nameTest)
    this.test;
    console.log(this.test)
    this.test.creatorUserId = this.userId;
    this.test.groupId = this.groupId;
    this.test.nameTest = this.name.value;
    console.log(this.test)
    this.testService.createTest(this.test).subscribe(id => {this.test.id = id;
    this.router.navigateByUrl('/test/'+ this.groupId+'/create/'+ this.test.id);});
 }

 navigateBack(){
  this.router.navigateByUrl('group/'+ this.groupId);
 }
}