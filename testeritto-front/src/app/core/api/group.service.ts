import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { Group } from '../models/group.model';
import { Test } from '../models/test.model';
import { User } from '../models/user.model';
import { Result } from '../models/result.model';
import { Remark } from '../models/remark.model';

@Injectable()
export class GroupService {
    group: Group;
    users: Array<User>;
    tests: Array<Test>;
    remarks: Array<Remark>;
    constructor(private httpClient: HttpClient) {
        
    }

    getGroup(id: BigInteger): Observable<Group> {
        return this.httpClient.get<Group>(`${environment.apiUrl}/group/` + id).pipe(
            map((group: Group) => {             
                return new Group().deserialize(group);
            })
        );
    }

    getUserResultsForTest(id: BigInteger): Observable<Result[]> {
        return this.httpClient.get<Result[]>(`${environment.apiUrl}/group/result/user/` + id).pipe(
            map(results => {
                return results.map(result => {
                    return new Result().deserialize(result)
                });
            })
        );        
    }

    getUsersInGroup(id: BigInteger): Observable<User[]> {
        return this.httpClient.get<User[]>(`${environment.apiUrl}/group/`+id+`/users`).pipe(
            map(users => {
                return users.map(user => {
                    return new User().deserialize(user);
                });
            })
        );
    }

    getAllRemarks(id: BigInteger): Observable<Remark[]> {
        return this.httpClient.get<Remark[]>(`${environment.apiUrl}/group/`+id+`/remarks`).pipe(
            map(remarks => {
                return remarks.map(remark => {
                    return new Remark().deserialize(remark);
                })
            })
        )
    }

    create(group: Group): Observable<BigInteger> {
        const headers = new HttpHeaders().set('content-type', 'application/json'); 
        return this.httpClient.post<BigInteger>(`${environment.apiUrl}/group/create`, group, {headers});   
    }

    deleteGroup(id: BigInteger): Observable<{}> {
        return this.httpClient.delete(`${environment.apiUrl}/group/`+ id);
    }
}