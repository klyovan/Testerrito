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
        return this.httpClient.get<User>(`${environment.apiUrl}/user/` + this.authService.getUserId()).pipe(
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
            phone: user.phone,
        };
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

    updateUserCredentials(userWithOldPswd: User, userUpdated: User): Observable<User> {
        const body = [{ id: userWithOldPswd.id,
            email: userWithOldPswd.email,
            password: userWithOldPswd.password},
            { id: userUpdated.id,
                email: userUpdated.email,
                password: userUpdated.password }] ;
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
                return new User().deserialize(user);
            }));
    }

    deleteUser(password: string): Observable<any> {
        return this.httpClient.delete<User>(`${environment.apiUrl}/profile/delete/user/` +
            this.authService.getUserId(),
            { params: {password: password}});
    }

    enterUserInGroup(userId: BigInteger, groupId: BigInteger): Observable<any> {
        return this.httpClient.get(`${environment.apiUrl}/user/enter/${groupId}/${userId}`);
    }

    private accessToken: string;
    register(user: User): Observable<any> {
        return this.httpClient.post(`${environment.apiUrl}/registration`,
            {
                lastName: user.lastName,
                firstName: user.firstName,
                email:user.email,
                password:user.password,
                phone: user.phone
            });
    }
    getByEmail(email:String):Observable<any>{
        return this.httpClient.get(
            `${environment.apiUrl}/registration/email/`+email);
    }

    getByPhone(phone:String):Observable<any>{
        return this.httpClient.get(
            `${environment.apiUrl}/registration/phone/`+phone);
    }

}
