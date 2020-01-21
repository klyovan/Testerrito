import { HttpInterceptor, HttpErrorResponse, HttpHandler, HttpRequest, HttpEvent } from '@angular/common/http';
import { throwError, Observable, empty } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable()
export class InterceptorService implements HttpInterceptor{
    static router;
    constructor(private router: Router) { InterceptorService.router = router; }
        
    handleError(error: any){
        if (Error instanceof HttpErrorResponse) {
            console.log(error.status);
        }
        else {
            if(error.status == 0 && error.statusText == "Unknown Error") { 
                InterceptorService.router.navigateByUrl('/error-page/503');             
            }                
        }
        return throwError(error);
    }
        
    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(req).pipe(
            retry(2),
            catchError(this.handleError)
        )
    };
}