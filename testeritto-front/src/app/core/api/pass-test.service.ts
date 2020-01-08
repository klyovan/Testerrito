import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Test} from '../models/test.model';
import {environment} from '../../../environments/environment';
import {Result} from '../models/result.model';

@Injectable({
  providedIn: 'root'
})
export class PassTestService {

  constructor(private httpClient: HttpClient) {}

  // public getTest(id, result): Observable<any> {
  // return this.httpClient.put(`${environment.apiUrl}/pass-test/test/` + id, result );
  // }

  // public getReplies(): Observable<any> {
  //   return this.httpClient.get(`${environment.apiUrl}/pass-test/reply`);
  // }

 public getTest(userId: BigInteger, id: BigInteger): Observable<any> {
  return this.httpClient.get(`${environment.apiUrl}/pass-test/${userId}/test/${id}`);
 }

}

