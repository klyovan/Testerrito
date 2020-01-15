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
import {MatDialog} from '@angular/material';
import {ModalRemarkComponent} from '../modal-remark/modal-remark.component';
import {Question} from '../core/models/question.model';


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
    answers: Array<Answer> = [];
    createdReply: BigInteger;
    results: Array<Result>;
    isQuestions = true;
    category: Category;
    isFirst = true;
    isTestNeedExpert = false;
    remarkText: string;
    remarkQuestion: string;
    reply: Reply;
    openAnswer: string;
    answer: Answer;
    panelOpenState: boolean;
    remark: Remark;
    selectedAnswer: Answer;
    remarkAnswer: string;


    constructor(private route: ActivatedRoute, private router: Router,
                private passTestService: PassTestService,
                public dialog: MatDialog) {
        this.route.params.subscribe((params) => {
            this.id = params.id;
            this.userId = params.userId;
        });

    }

    ngOnInit() {
        this.passTestService.getTest(this.userId, this.id).subscribe((test: Test) => {

            this.test = test;
            this.test.questions.forEach((question: Question) => {
                if (question.typeQuestion === 'OPEN') {
                    this.isTestNeedExpert = true;
                }
            });
        });


        window.addEventListener('beforeunload', function(e) {
            const confirmationMessage = '\o/';
            console.log('refresh..');
            e.returnValue = confirmationMessage;
            return confirmationMessage;
        });


    }

    incrementIndex() {
        this.isFirst = false;
        if (this.questionId < this.test.questions.length - 1) {

            this.reply = new Reply;

            if (this.selectedAnswer !== undefined) {
                this.answers.push(this.selectedAnswer);
                this.selectedAnswer = undefined;
            }

            this.reply.replyList = this.answers.slice();
            this.passTestService.addReply(this.reply).subscribe(value => this.createdReply = value);

            this.answers = [];
            this.reply = new Reply();
            this.openAnswer = null;
            this.remarkAnswer = null;
            return this.questionId++;
        } else {
            this.reply = new Reply;
            if (this.selectedAnswer !== undefined) {
                this.answers.push(this.selectedAnswer);
                this.selectedAnswer = undefined;
            }

            this.reply.replyList = this.answers.slice();
            this.passTestService.addReply(this.reply).subscribe(value => this.createdReply = value);

            this.questionId++;
            this.buttonType = 2;
            this.answers = [];
            this.remarkAnswer = null;
            this.openAnswer = null;
        }
        this.openAnswer = null;

    }


    decrementIndex() {
        if (this.questionId > 0) {

            this.answers = [];
            this.questionId--;
            this.buttonType = 1;
            this.remarkAnswer = null;
        }
    }


    testEnd() {
        this.isFirst = true;
        this.isQuestions = false;
        this.passTestService.getReplies().subscribe((results) => this.results = results);

        this.buttonType = 3;
    }


    getCategoryName(categoryId: BigInteger): string {
        this.passTestService.getCategory(categoryId).subscribe((category: Category) => category = this.category);
        console.log(this.category.nameCategory);
        return this.category.nameCategory;

    }

    showReport(): void {
        this.remarkQuestion = this.test.questions[this.questionId].textQuestion;

        const dialogRef = this.dialog.open(ModalRemarkComponent, {
            width: '400px',
            data: {remarkQuestion: this.remarkQuestion, remarkText: this.remarkText}
        });

        this.remark = new Remark();
        this.remark.text = '';

        dialogRef.afterClosed().subscribe(result => {
            if (result) {
                this.remark.text = result;
                this.remark.userSenderId = this.userId;
                this.remark.userRecipientId = this.test.creatorUserId;
                this.remark.questionId = this.test.questions[this.questionId].id;
                this.passTestService.addRemark(this.remark).subscribe();
                this.remark = undefined;
                this.remarkAnswer = dialogRef.componentInstance.closeMessage;

            }
            console.log('LOLOLOL');
        });
    }



}
