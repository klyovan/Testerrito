import {Injectable} from '@angular/core';
import {CanActivate, CanLoad, Route, UrlSegment, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router} from '@angular/router';
import {Observable} from 'rxjs';
import {AuthService} from '../core/api/auth.service';

@Injectable({
    providedIn: 'root'
})
export class AuthGuard implements CanActivate, CanLoad {


    constructor(private authService: AuthService, private router: Router) {
    }

    canActivate(
        next: ActivatedRouteSnapshot,
        state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
        if (this.authService.isLoggedIn) {
            return true;
        } else {
            this.authService.url = window.location.pathname;
            console.log(this.authService.url);
            this.router.navigate(['/login']);
            return false;
        }
    }

    canLoad(
        route: Route,
        segments: UrlSegment[]): Observable<boolean> | Promise<boolean> | boolean {
        return true;
    }
}
