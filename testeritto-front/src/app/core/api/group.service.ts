import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { Group } from '../models/group.model';
import { Test } from '../models/test.model';
import { User } from '../models/user.model';
import { Result } from '../models/result.model';

@Injectable()
export class GroupService {
    constructor(private httpClient: HttpClient) {
        
    }

    getGroup(id: BigInteger): Observable<Group> {
        return this.httpClient.get<Group>(`${environment.apiUrl}/group/` + id).pipe(
            map((group: Group) => {
                if(group.users == null)
                    group.users = new Array<User>();
                if(group.tests == null)
                    group.tests = new Array<Test>();
                return new Group().deserialize(group);
            })
        );
    }

    getUserResultsForTest(id: BigInteger): Observable<Result[]> {
        return this.httpClient.get<Result[]>(`${environment.apiUrl}/group/result/user/` + id).pipe(
            map(results => {
                return results.map(item => {
                    return new Result().deserialize(item)
                });
            })
        );
        
    }
}