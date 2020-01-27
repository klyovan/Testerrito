import {Component, Input, OnInit} from '@angular/core';
import {PassTestService} from '../core/api/pass-test.service';
import {UserService} from '../core/api/user.service';
import {ActivatedRoute, Router} from '@angular/router';
import {Result} from '../core/models/result.model';
import {User} from '../core/models/user.model';
import {Observable} from 'rxjs';
import {Test} from '../core/models/test.model';
import {Answer} from '../core/models/answer.model';
import {ResultService} from '../core/api/result.service';
import {Category} from '../core/models/category.model';
import {Remark} from '../core/models/remark.model';
import {Reply} from '../core/models/reply.model';


@Component({
  selector: 'app-pass-test',
  templateUrl: './pass-test.component.html',
  styleUrls: ['./pass-test.component.css']
})
export class PassTestComponent implements OnInit {
  id: BigInteger;
  test: Test;
  userId: BigInteger;
  questionId = 0;
  user: User;
  buttonType = 1;
  answers: Array<Answer> = new Array<Answer>();
  isExist = false;
  createdReply: BigInteger;
  createdRemark: BigInteger;
  results: Array<Result>;
  testResult: Result;
  testReplies: Map<string, Array<string>>;
  isQuestions = true;
  category: Category;
  isFirst = true;
  remark: Remark;
  reply: Reply;



  constructor(private route: ActivatedRoute, private router: Router,
              private passTestService: PassTestService, private userService: UserService, private resultService: ResultService) {
    this.route.params.subscribe((params) => {
      this.id = params['id'];
      this.userId = params['userId'];
    });

  }

  ngOnInit() {
    this.passTestService.getTest(this.userId, this.id).subscribe((test: Test) => this.test = test);
    window.addEventListener('beforeunload', function (e) {
      let confirmationMessage = '\o/';
      console.log('cond');
      e.returnValue = confirmationMessage;
      return confirmationMessage;
    });

  }

  incrementIndex() {
    this.isFirst = false;
    if (this.questionId < this.test.questions.length - 1) {

      console.log(this.questionId + "/questionIF");

      this.reply = new Reply;

      this.reply.replyList = this.answers;

      this.passTestService.addReply(this.reply).subscribe(value => this.createdReply = value);

      console.log(this.createdReply);
      this.answers = [];
      this.reply = new Reply();
      return this.questionId++;
    } else {
      this.reply = new Reply;
      this.reply.replyList = this.answers;
      this.passTestService.addReply(this.reply).subscribe(value => this.createdReply = value);
      this.questionId++;
      this.buttonType = 2;
    }
  }


  decrementIndex() {
    if (this.questionId > 0) {

      this.answers = [];
      this.questionId--;
      this.buttonType = 1;
    }
  }

  addReply(answer: Answer) {
    this.isExist = false;
    if (this.answers.length > 0) {
      this.answers.forEach((value: Answer) => {
          if (value.id === answer.id) {
            console.log('already exist ' + answer.textAnswer + ' id =' + answer.id);
            this.isExist = true;
            return;
          }
        }
      );
      if (this.isExist) {
        return;
      }
      console.log(this.answers + ' Added ' + answer.textAnswer);
      return this.answers.push(answer);
    } else {
      console.log('frist add');
      return this.answers.push(answer);
    }
  }

  addReplyWithOneAnswer(answer: Answer) {
    this.isExist = false;
    if (this.answers.length > 0) {
      this.answers.forEach((value: Answer) => {
          if (value.id === answer.id) {
            console.log('already exist ' + answer.textAnswer + ' id =' + answer.id);
            this.isExist = true;
            return;
          }
        }
      );
      if (this.isExist) {
        return;
      }
      console.log(this.answers + ' Added ' + answer.textAnswer);
      this.answers = [];
      return this.answers.push(answer);
    } else {
      console.log('frist add');
      return this.answers.push(answer);
    }
  }


  testEnd() {
    console.log('End lol ');
    this.isFirst = true;
    this.isQuestions = false;
    this.passTestService.getReplies().subscribe((results) => this.results = results);

    console.log(this.results);
    this.buttonType = 3;
  }



  getCategoryName(categoryId: BigInteger): string {
    this.passTestService.getCategory(categoryId).subscribe((category: Category) => category = this.category );
    console.log(this.category.nameCategory);
    return this.category.nameCategory;

  }

  showReport() {
    let report = prompt('Please enter your report for question \"'+ this.test.questions[this.questionId].textQuestion +'\" :', '');

    if (report != null && report.length > 0) {
      // txt = "User cancelled the prompt.";


      // txt = "Hello " + report + "! How are you today?";
      this.remark = new Remark();

      this.remark.userRecipientId = this.test.creatorUserId;
      this.remark.text = report;
      this.remark.userSenderId = this.userId;
      this.remark.questionId = this.test.questions[this.questionId].id;
      this.passTestService.addRemark(this.remark).subscribe((value => this.createdRemark = value));

    } else if (report.length === 0 || report == null) {
      alert('Input your report please!');
      return false;

    }
    else {
      console.log(report.length);
      alert('Entry Cancelled By User');
      return false;

    }

  }





  // getValues(results: Array<Result>):[string, Array<Result>] {
  //
  // }
}
