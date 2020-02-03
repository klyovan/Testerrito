import {Component, OnDestroy, OnInit} from '@angular/core';
import {PassTestService} from '../core/api/pass-test.service';
import {ActivatedRoute, NavigationStart, Router} from '@angular/router';
import {Result} from '../core/models/result.model';
import {User} from '../core/models/user.model';
import {Test} from '../core/models/test.model';
import {Answer} from '../core/models/answer.model';
import {Remark} from '../core/models/remark.model';
import {Reply} from '../core/models/reply.model';
import {MatDialog} from '@angular/material';
import {ModalRemarkComponent} from '../modal-remark/modal-remark.component';
import {FormControl, Validators} from '@angular/forms';
import {browserRefresh} from '../app.component';


@Component({
    selector: 'app-pass-test',
    templateUrl: './pass-test.component.html',
    styleUrls: ['./pass-test.component.css']
})
export class PassTestComponent implements OnInit, OnDestroy {
    id: BigInteger;
    test: Test;
    userId: BigInteger;
    questionId = 0;
    user: User;
    buttonType = 1;
    results: Array<Result>;
    isQuestions = true;
    isFirst = true;
    // isTestNeedExpert = false;
    remarkText: string;
    remarkQuestion: string;
    reply: Reply;
    // openAnswer: string;
    answer: Answer;
    remark: Remark;
    selectedAnswer: Answer;
    selectedAnswers: Array<Answer> = [];
    remarkAnswer: string;
    selectionForm = new FormControl(null, Validators.required);
    radioForm = new FormControl(null, [Validators.required]);
    markedAnswers: Map<BigInteger, Array<Answer>> = new Map();
    browserRefresh: boolean;


    // openAnswerForm = new FormControl('', [Validators.required]);

    constructor(private route: ActivatedRoute, private router: Router,
                private passTestService: PassTestService,
                public dialog: MatDialog) {
        this.route.params.subscribe((params) => {
            this.id = params.id;
            this.userId = params.userId;
        });


    }

    ngOnInit() {
        this.browserRefresh = browserRefresh;
        if (this.browserRefresh === true) {
            this.router.navigate(['/group']);
        }


        if (this.passTestService.notPassedTest === undefined && this.browserRefresh === false) {
            this.passTestService.getTest(this.userId, this.id).subscribe((test: Test) => {
                this.test = test;

                // this.test.questions.forEach((question: Question) => {
                //     // if (question.typeQuestion === 'OPEN') {
                //     //     this.isTestNeedExpert = true;
                //     // }
                // });
            });
        } else {
            {
                this.test = this.passTestService.notPassedTest;
                this.passTestService.notPassedTest = undefined;
            }
        }



    }

    ngOnDestroy(): void {
    }


    incrementIndex() {
        this.isFirst = false;
        this.reply = new Reply;

        if (this.questionId < this.test.questions.length - 1) {

            // if (this.selectedAnswers.length !== 0 || this.selectedAnswer !== undefined) {
            if (this.selectedAnswer !== undefined) {
                this.selectedAnswers.push(this.selectedAnswer);
            }

            this.reply.replyList = this.selectedAnswers.slice();
            this.passTestService.addReply(this.reply).subscribe();

            this.markedAnswers.set(this.selectedAnswers[0].questionId, this.selectedAnswers);


            this.questionId++;


            // } else if (this.openAnswer !== null) {
            //     this.answer = new Answer();
            //     this.answer.score = 0;
            //     this.answer.questionId = this.test.questions[this.questionId].id;
            //     this.answer.textAnswer = this.openAnswer;
            //
            //
            //     this.passTestService.addAnswer(this.answer).subscribe((createdAnswerId: BigInteger) => {
            //         this.answer.id = createdAnswerId;
            //         this.selectedAnswers.push(this.answer);
            //         this.reply.replyList = this.selectedAnswers.slice();
            //         this.passTestService.addReply(this.reply).subscribe();
            //
            //         this.markAnswer(this.selectedAnswers);
            //
            //         this.selectedAnswers = [];
            //     });
            //     return this.questionId++;
            // }


        } else {

            if (this.selectedAnswer !== undefined) {
                this.selectedAnswers.push(this.selectedAnswer);
            }

            this.reply.replyList = this.selectedAnswers.slice();
            this.passTestService.addReply(this.reply).subscribe(() => {

                this.markedAnswers.set(this.selectedAnswers[0].questionId, this.selectedAnswers);


            });

            this.buttonType = 2;


            // } else if (this.openAnswer !== null && this.selectedAnswers === undefined) {
            //     this.answer = new Answer();
            //     this.answer.score = 0;
            //     this.answer.questionId = this.test.questions[this.questionId].id;
            //     this.answer.textAnswer = this.openAnswer;
            //
            //
            //     this.passTestService.addAnswer(this.answer).subscribe((createdAnswerId: BigInteger) => {
            //         this.answer.id = createdAnswerId;
            //         this.selectedAnswers.push(this.answer);
            //         this.reply.replyList = this.selectedAnswers.slice();
            //         this.passTestService.addReply(this.reply).subscribe(() => {
            //
            //             this.markAnswer(this.selectedAnswers);
            //
            //             this.selectedAnswers = [];
            //         });
            //         this.buttonType = 2;
            //
            //         return this.questionId++;
            //     });
            // }

        }
        this.remarkAnswer = null;

        this.checkIfAnswerExist();

    }

    checkIfAnswerExist() {
        this.selectedAnswers = [];
        this.selectedAnswer = undefined;
        this.radioForm = new FormControl(null, [Validators.required]);
        this.selectionForm = new FormControl(null, [Validators.required]);
        if (this.markedAnswers.get(this.test.questions[this.questionId].id) &&
            this.test.questions[this.questionId].typeQuestion === 'ONE_ANSWER') {
            this.radioForm.setValue(this.markedAnswers.get(this.test.questions[this.questionId].id)[0]);
            this.selectedAnswer = this.markedAnswers.get(this.test.questions[this.questionId].id)[0];

        } else if (this.markedAnswers.get(this.test.questions[this.questionId].id) &&
            this.test.questions[this.questionId].typeQuestion === 'MULTIPLE_ANSWER') {
            this.selectionForm.setValue(this.markedAnswers.get(this.test.questions[this.questionId].id));
            this.selectedAnswers = this.markedAnswers.get(this.test.questions[this.questionId].id);
        }

    }


    decrementIndex() {
        if (this.questionId > 0) {
            if (this.selectedAnswer !== undefined || this.selectedAnswers.length !== 0) {
                if (this.selectedAnswer !== undefined) {
                    this.selectedAnswers.push(this.selectedAnswer);
                }

                this.reply.replyList = this.selectedAnswers.slice();
                this.passTestService.addReply(this.reply).subscribe(() => {
                    this.markedAnswers.set(this.selectedAnswers[0].questionId, this.selectedAnswers);
                    this.questionId--;
                    this.checkIfAnswerExist();

                });
            } else {
                console.log('prostoDDecr');
                this.questionId--;
                this.checkIfAnswerExist();
            }


        }


        this.remarkAnswer = null;

        if (this.buttonType === 2) {
            this.buttonType = 1;
        } else if (this.questionId === 0) {
            this.isFirst = true;
        }

    }


    testEnd() {
        this.isFirst = true;
        this.router.navigate(['/group', this.test.groupId]);

        this.passTestService.getReplies().subscribe(() => {
            this.test = undefined;
        });
        this.isQuestions = false;
        this.buttonType = undefined;


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

    // getErrorMessage() {
    //     return this.openAnswerForm.hasError('required') ? 'You must enter answer' :
    //         'Enter more than 10 characters!';
    // }

    // getSelectionErrorMessage() {
    //     return this.selectionForm.hasError('required') ? 'You must select  answer' :
    //         'Select answer';
    // }
    //
    // getRadioErrorMessage() {
    //     return this.radioForm.hasError('required') ? 'You must select answer' :
    //         'Select answer';
    // }

}
