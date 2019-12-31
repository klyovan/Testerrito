import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../models/user.model';
import { AuthService } from './auth.service';
import { environment } from 'src/environments/environment';
import { map, tap } from 'rxjs/operators';
import { Group } from '../models/group.model';
import { Result } from '../models/result.model';

@Injectable()
export class UserService {
  constructor(private httpClient: HttpClient,
              private authService: AuthService) {}

  getUser(): Observable<User> {
    return this.httpClient.get<User>(`${environment.apiUrl}/user/email/` + this.authService.getUserEmail()).pipe(
      map((user: User) => {
        if (user.createdGroups == null) {
          user.createdGroups = Array<Group>();
        }
        if (user.groups == null) {
          user.groups = Array<Group>();
        }
        if (user.results == null) {
          user.results = Array<Result>();
        }
        return new User().deserialize(user);
      }));
  }

  updateUser(user: User): Observable<User> {
    const body = {  id: user.id,
                    lastName: user.lastName,
                    firstName: user.firstName,
                    phone: user.phone };
    return this.httpClient.put<User>(`${environment.apiUrl}/user`, body).pipe(
      // tslint:disable-next-line: no-shadowed-variable
      map((user: User) => {
        if (user.createdGroups == null) {
          user.createdGroups = Array<Group>();
        }
        if (user.groups == null) {
          user.groups = Array<Group>();
        }
        if (user.results == null) {
          user.results = Array<Result>();
        }
        return new User().deserialize(user);
      }));
  }

  updateUserCredentials(user: User): Observable<User> {
    console.log("Before Credentials:", user);
    const body = {  id: user.id,
                    email: user.email,
                    password: user.password  };
    return this.httpClient.put<User>(`${environment.apiUrl}/user/credentials`, body).pipe(
      // tslint:disable-next-line: no-shadowed-variable
      map((user: User) => {
        if (user.createdGroups == null) {
          user.createdGroups = Array<Group>();
        }
        if (user.groups == null) {
          user.groups = Array<Group>();
        }
        if (user.results == null) {
          user.results = Array<Result>();
        }
        console.log("Credentials:", user);
        return new User().deserialize(user);
      }));
    // this.authService.logout();
  }
}
