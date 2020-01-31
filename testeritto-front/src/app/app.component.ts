import {Component, OnDestroy, OnInit} from '@angular/core';
import {AuthService} from './core/api/auth.service';
import {NavigationStart, Router} from '@angular/router';
import {Subscription} from 'rxjs';

export let browserRefresh = false;


@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent implements OnDestroy {
    title = 'testeritto-front';
    subscription: Subscription;
    showModal: boolean;

    get isLoggedIn() {
        return this.authService.isLoggedIn;
    }

    constructor(private router: Router, private authService: AuthService) {
        this.subscription = router.events.subscribe((event) => {
            if (event instanceof NavigationStart) {
                browserRefresh = !router.navigated;
            }
        });
    }

    ngOnDestroy(): void {
        this.subscription.unsubscribe();
    }


}
