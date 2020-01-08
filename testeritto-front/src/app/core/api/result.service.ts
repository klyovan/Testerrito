import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Result } from '../models/result.model';
import { environment } from 'src/environments/environment';
import { map, tap } from 'rxjs/operators';

@Injectable()
export class ResultService {
    constructor(private httpClient: HttpClient) {
        
    }

    getResult(id: BigInteger): Observable<Result> {
        return this.httpClient.get<Result>(`${environment.apiUrl}/group/result/` + id).pipe(
            map((result: Result) => {               
                return new Result().deserialize(result);
            })
        );
    }
}
