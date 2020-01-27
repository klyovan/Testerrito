import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpInterceptor, HttpResponse,
  HttpErrorResponse, HttpUserEvent, HttpSentEvent, HttpHeaderResponse, HttpProgressEvent } from '@angular/common/http';
import { Observable, throwError, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { AuthService } from './auth.service';
import { tokenName } from '@angular/compiler';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {

  constructor(private authService: AuthService) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler)
    : Observable<HttpSentEvent | HttpHeaderResponse | HttpProgressEvent | HttpResponse<any> | HttpUserEvent<any>> {
  //  console.log(request);
    let pattern =new RegExp("^https://localhost:8443/registration+.*");
    //'https://localhost:8443/registration'
    if(request.url.match(pattern)){

      return next.handle(request);
    }
    return next.handle(request
      .clone({
      headers: request.headers.append('Authorization', 'Bearer ' + this.authService.getAccessToken())
    })
    ).pipe(
      catchError((error: HttpErrorResponse) => this.handleAuthError(error)));
  }

  private handleAuthError(err: HttpErrorResponse): Observable<any> {
    if (err.status === 401 || err.status === 403) {
      this.authService.logout();
    }

    return throwError(err);
  }
}
