import { Injectable } from '@angular/core';
import { tap } from 'rxjs/operators';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';

import { environment } from 'src/environments/environment';
import { JwtHelperService } from '@auth0/angular-jwt';
import { deprecate } from 'util';



@Injectable()
export class AuthService {
  private accessToken: string;

  private readonly tokenEndpoint = `${environment.apiUrl}/login`;
  private readonly tokenStorageKey = 'accessToken';
  public url: string;

  get isLoggedIn(): boolean {
    return !!this.accessToken;
  }

  constructor(private httpClient: HttpClient,
              private router: Router) {
    this.loadToken();
  }

  login(login: string, password: string): Observable<any> {
    // tslint:disable-next-line: object-literal-shorthand
    const body = {userEmail: login, password: password};
    return this.httpClient.post(`${this.tokenEndpoint}`, body, {observe: 'response' }).pipe(
      tap((response: any) => {
        this.accessToken = response.headers.get('authorization');
        this.saveToken(response.headers.get('authorization'));
      })
    );
  }

  logout() {
    localStorage.removeItem(this.tokenStorageKey);
    this.accessToken = null;
    this.url = undefined;
    this.router.navigate(['/login']);
  }

  getUserRole(): string {
    const jwtService = new JwtHelperService();
    const decodedToken = jwtService.decodeToken(this.accessToken);
    return decodedToken['http://schemas.microsoft.com/ws/2008/06/identity/claims/role'];
  }

  getUserEmail(): string {
    const jwtService = new JwtHelperService();
    const decodedToken = jwtService.decodeToken(this.accessToken);
    // tslint:disable-next-line: no-string-literal
    return decodedToken['sub'];
  }

  getUserId(): string {
    const jwtService = new JwtHelperService();
    const decodedToken = jwtService.decodeToken(this.accessToken);
    // tslint:disable-next-line: no-string-literal
    return decodedToken['userId'];
  }

  private saveToken(accessToken: string) {
    localStorage.setItem(this.tokenStorageKey, accessToken);
  }

  private loadToken() {
    this.accessToken = localStorage.getItem(this.tokenStorageKey);
  }

  public getAccessToken(): string {
    return this.accessToken;
  }
}
