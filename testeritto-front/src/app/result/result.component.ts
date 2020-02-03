import {Component, OnInit} from '@angular/core';
import {Subscription} from 'rxjs';
import {Result} from '../core/models/result.model';
import {ActivatedRoute, Router} from '@angular/router';
import {ResultService} from '../core/api/result.service';
import {Category} from '../core/models/category.model';
import {User} from '../core/models/user.model';
import {UserService} from '../core/api/user.service';
import {ChartDataSets, ChartType, RadialChartOptions} from 'chart.js';
import {Label} from 'ng2-charts';
import {Test} from '../core/models/test.model';
import {TestService} from '../core/api/test.service';

@Component({
    selector: 'app-result',
    templateUrl: './result.component.html',
    styleUrls: ['./result.component.css']
})
export class ResultComponent implements OnInit {
    results: Array<Result>;
    status: string;
    date: Date;
    groupId: BigInteger;
    panelOpenState: boolean;
    categories: Array<Category> = [];
    user: User;
    test: Test;
    openResult = false;

    radarChartOptions: RadialChartOptions = {
        responsive: true,
        maintainAspectRatio: false,
        animation: {duration: 2000}
    };
    radarChartLabels: Label[] = [];

    radarChartData: ChartDataSets[] = [
        {data: [], label: 'Categories'}
    ];
    radarChartType: ChartType = 'radar'; // if categories<3 'pie'
    scores = [];
    dataSet: ChartDataSets[] = [];
    radarColors = [
        {
            backgroundColor: [
                '#2e8b57',
                '#ffae51',
                '#007777'
            ]
        }
    ];


    constructor(private route: ActivatedRoute,
                private router: Router,
                private resultService: ResultService,
                private userService: UserService,
                private testService: TestService) {
        if (this.resultService.results === undefined) {
            route.params.subscribe(params => this.groupId = params['groupId']);
            this.router.navigateByUrl('/group/' + this.groupId);
        }

    }

    ngOnInit() {

        this.userService.getUser().subscribe((user: User) => {
            this.user = user;
            this.testService.getTest(this.results[0].testId).subscribe((test: Test) => {
                this.test = test;
            });
        });
        this.results = this.resultService.results;
        this.date = this.results.find(element => element.id !== undefined).date;
        this.status = this.results.find(element => element.id !== undefined).status;

        for (const result of this.results) {
            this.resultService.getCategory(result.categoryId).subscribe((category: Category) => {
                    this.categories.push(category);
                    this.radarChartLabels.push(category.nameCategory);
                }
            );

        }


        this.results.forEach((result: Result) => {
            this.scores.push(result.score);
            this.radarChartData[0].data.push(result.score);
        });
        if (this.radarChartData[0].data.length < 3) {
            this.radarChartType = 'pie';
        }

        // this.scores.forEach((score) => {
        //     // this.radarChartData
        // );

        // console.log(this.radarChartData);
        // console.log(this.dataSet);


    }


}
