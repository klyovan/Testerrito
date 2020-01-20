import {Component, OnInit} from '@angular/core';
import {Group} from '../core/models/group.model';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs';
import {UserService} from '../core/api/user.service';
import {User} from '../core/models/user.model';
import {GroupService} from '../core/api/group.service';
import {Result} from '../core/models/result.model';
import {ResultService} from '../core/api/result.service';
import {DatePipe} from '@angular/common';
import {PassTestService} from '../core/api/pass-test.service';
import {Test} from '../core/models/test.model';

@Component({
    selector: 'app-group',
    templateUrl: './group.component.html',
    styleUrls: ['./group.component.css']
})
export class GroupComponent implements OnInit {
    group: Group;
    user: User;
    results: Array<Result> = new Array();
    finalResult: Map<Date, String>;
    loading: Boolean = false;
    testsLoaded: Boolean = false;
    showResult: Boolean = false;
    infoIfNoResult: String;
    selectedTest: BigInteger;
    test: Test;

    constructor(private route: ActivatedRoute,
                private userService: UserService,
                private groupService: GroupService,
                private resultService: ResultService,
                private passTestService: PassTestService,
                private router: Router,
                private datePipe: DatePipe) {
        this.group = new Group;
        route.params.subscribe(params => this.group.id = params['groupId']);
    }

    ngOnInit() {
        var check;
        this.userService.getUser().subscribe((user: User) => {
            this.user = user;
            check = user.groups.find(element => element.id == this.group.id);
            if (check != undefined) {
                this.loading = true;
                this.groupService.getGroup(this.group.id).subscribe((group: Group) => {
                    this.group = group;
                    this.testsLoaded = true;
                    this.groupService.tests = this.group.tests;
                });
                this.groupService.getUserResultsForTest(this.user.id).subscribe(data => {
                    this.user.results = data;
                });
            } else {
                this.router.navigate(['/group']);
            }
        });
    }

    showResultsOnTest(id: BigInteger) {
        this.selectedTest = id;
        this.results = new Array();
        var result = this.user.results.filter(element => element.testId == this.selectedTest);
        result.forEach(element => {
            if (this.results != undefined && this.results.find(elem => elem.status == element.status && elem.date == element.date) == undefined) {
                this.results.push(element);
            }

        });
        if (this.results.length > 0) {
            this.showResult = true;
        } else {
            this.showResult = false;
        }
    }

    copyLinkToClipboard() {
        document.addEventListener('copy', (e: ClipboardEvent) => {
            e.clipboardData.setData('text/plain', (this.group.link));
            e.preventDefault();
            document.removeEventListener('copy', null);
        });
        document.execCommand('copy');
    }

    passTest(id: BigInteger) {
        var nowDate = this.datePipe.transform(new Date(), 'yyyy-MM-dd');
        var checkResult = this.user.results.find(element => element.date.toString() == nowDate && element.testId == id);
        if(checkResult == undefined)
        this.router.navigateByUrl('/pass-test/' + this.user.id + '/test/' + id);
        else alert("You already passed test today. Come back tomorrow :)");
    }

    finishTest(testId: BigInteger) {
        this.passTestService.getFinishTest(this.user.id, testId).subscribe((test: Test) => {
            this.test = test;
            console.log(test.questions);
        });

        this.router.navigate(['/pass-test', this.user.id, 'test', testId]);

    }

    showDetailsOnResult(date: Date) {
        this.results = this.user.results.filter(element => element.testId == this.selectedTest
            && element.date == date && element.status == 'PASSED');
        this.resultService.results = this.results;
        this.router.navigateByUrl('/group/' + this.group.id + '/results');
    }

    showUsersInGroup() {
        this.groupService.getUsersInGroup(this.group.id).subscribe(data => {
            this.groupService.users = data;
            this.router.navigateByUrl('/group/' + this.group.id + '/users');
        });
    }

    showAllRemarks() {
        this.groupService.getAllRemarks(this.group.id).subscribe(data => {
            this.groupService.remarks = data;
            this.router.navigateByUrl('/group/' + this.group.id + '/remarks');
        })
    }
}
