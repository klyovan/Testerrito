import {Component, Input, OnInit} from '@angular/core';
import {PassTestService} from '../core/api/pass-test.service';
import {UserService} from '../core/api/user.service';
import {ActivatedRoute, Router} from '@angular/router';
import {Result} from '../core/models/result.model';
import {User} from '../core/models/user.model';
import {Test} from '../core/models/test.model';
import {Answer} from '../core/models/answer.model';
import {Category} from '../core/models/category.model';
import {Remark} from '../core/models/remark.model';
import {Reply} from '../core/models/reply.model';
import {MatDialog} from '@angular/material';
import {ModalRemarkComponent} from '../modal-remark/modal-remark.component';
import {Question} from '../core/models/question.model';
import {FormControl, Validators} from '@angular/forms';


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
    results: Array<Result>;
    isQuestions = true;
    isFirst = true;
    isTestNeedExpert = false;
    remarkText: string;
    remarkQuestion: string;
    reply: Reply;
    openAnswer: string;
    answer: Answer;
    remark: Remark;
    selectedAnswer: Answer;
    selectedAnswers: Array<Answer> = [];
    remarkAnswer: string;
    openAnswerForm = new FormControl('', [Validators.required]);
    selectionForm = new FormControl(Validators.required);
    radioForm = new FormControl('', [Validators.required]);
    markedAnswers: Map<BigInteger, Array<Answer>> = new Map();

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
                    this.isTestNeedExpert = true; //// true nado
                }
            });
            // this.selectedAnswer = test.questions[0].answers[0];
        });


        window.addEventListener('beforeunload', function(e) {
            const confirmationMessage = '\o/';
            console.log('refresh..');
            e.returnValue = confirmationMessage;
            return confirmationMessage;
        });


    }

    markAnswer(markedAnswers: Array<Answer>): void {
        this.markedAnswers.set(markedAnswers[0].questionId, markedAnswers);
        console.log(this.markedAnswers);
    }


    incrementIndex() {
        this.isFirst = false;
        this.reply = new Reply;

        if (this.questionId < this.test.questions.length - 1) {

            if (this.selectedAnswers.length !== 0 || this.selectedAnswer !== undefined) {
                if (this.selectedAnswer !== undefined) {
                    this.selectedAnswers.push(this.selectedAnswer);
                    this.selectedAnswer = undefined;
                }
                console.log(this.selectedAnswers + 'Aha');
                this.reply.replyList = this.selectedAnswers.slice();
                this.passTestService.addReply(this.reply).subscribe();

                this.markAnswer(this.selectedAnswers);

                this.selectedAnswers = [];

                return this.questionId++;

            } else if (this.openAnswer !== null) {
                this.answer = new Answer();
                this.answer.score = 0;
                this.answer.questionId = this.test.questions[this.questionId].id;
                this.answer.textAnswer = this.openAnswer;


                this.passTestService.addAnswer(this.answer).subscribe((createdAnswerId: BigInteger) => {
                    this.answer.id = createdAnswerId;
                    this.selectedAnswers.push(this.answer);
                    this.reply.replyList = this.selectedAnswers.slice();
                    this.passTestService.addReply(this.reply).subscribe();

                    this.markAnswer(this.selectedAnswers);

                    this.selectedAnswers = [];
                });
                return this.questionId++;
            }


        } else {

            if (this.selectedAnswer !== undefined || this.selectedAnswers !== undefined) {
                if (this.selectedAnswer !== undefined) {
                    this.selectedAnswers.push(this.selectedAnswer);
                    this.selectedAnswer = undefined;
                }

                this.reply.replyList = this.selectedAnswers.slice();
                this.passTestService.addReply(this.reply).subscribe(() => {

                    this.markAnswer(this.selectedAnswers);

                    this.selectedAnswers = [];
                });

                this.buttonType = 2;

                return this.questionId++;
            } else if (this.openAnswer !== null && this.selectedAnswers === undefined) {
                this.answer = new Answer();
                this.answer.score = 0;
                this.answer.questionId = this.test.questions[this.questionId].id;
                this.answer.textAnswer = this.openAnswer;


                this.passTestService.addAnswer(this.answer).subscribe((createdAnswerId: BigInteger) => {
                    this.answer.id = createdAnswerId;
                    this.selectedAnswers.push(this.answer);
                    this.reply.replyList = this.selectedAnswers.slice();
                    this.passTestService.addReply(this.reply).subscribe(() => {

                        this.markAnswer(this.selectedAnswers);

                        this.selectedAnswers = [];
                    });
                    this.buttonType = 2;

                    return this.questionId++;
                });
            }

        }
        this.remarkAnswer = null;
        this.selectedAnswers = [];
        this.openAnswer = null;

    }


    decrementIndex() {
        if (this.questionId > 0) {

            this.selectedAnswers = [];
            this.questionId--;
            this.buttonType = 1;
            this.remarkAnswer = null;
        }
    }


    testEnd() {
        this.isFirst = true;

        this.passTestService.getReplies().subscribe((results) => this.results = results);
        this.isQuestions = false;
        this.buttonType = undefined;
        if (!this.isTestNeedExpert) {
            this.router.navigate(['/group']);
        }
    }


    // getCategoryName(categoryId: BigInteger): string {
    //     this.passTestService.getCategory(categoryId).subscribe((category: Category) => category = this.category);
    //     console.log(this.category.nameCategory);
    //     return this.category.nameCategory;
    //
    // }

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
                this.remark.viewed = false;
                this.passTestService.addRemark(this.remark).subscribe();
                this.remark = undefined;
                this.remarkAnswer = dialogRef.componentInstance.closeMessage;

            }
        });
    }

    getErrorMessage() {
        return this.openAnswerForm.hasError('required') ? 'You must enter answer' :
            'Enter more than 10 characters!';
    }

    getSelectionErrorMessage() {
        return this.selectionForm.hasError('required') ? 'You must select  answer' :
            'Select answer';
    }

    getRadioErrorMessage() {
        return this.radioForm.hasError('required') ? 'You must select answer' :
            'Select answer';
    }

}
