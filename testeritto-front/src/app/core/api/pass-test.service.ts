import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../../environments/environment';
import {Answer} from '../models/answer.model';
import {Remark} from '../models/remark.model';
import {Reply} from '../models/reply.model';

@Injectable({
  providedIn: 'root'
})
export class PassTestService {

  constructor(private httpClient: HttpClient) {}


 public getTest(userId: BigInteger, id: BigInteger): Observable<any> {
  return this.httpClient.get(`${environment.apiUrl}/pass-test/${userId}/test/${id}`);
 }

 public addReply(reply: Reply): Observable<any> {
    return this.httpClient.post(`${environment.apiUrl}/pass-test/reply`, reply);
 }

  public updateReply(answers: Array<Answer>): Observable<any> {
    return this.httpClient.put(`${environment.apiUrl}/pass-test/updatereply`, answers);
  }


 public getReplies(): Observable<any> {
    return this.httpClient.get(`${environment.apiUrl}/pass-test/reply`);
 }

  public getCategory(categoryId: BigInteger ): Observable<any> {
    return this.httpClient.get(`${environment.apiUrl}/test/category/${categoryId}`);
 }

 public addRemark(remark: Remark): Observable<any> {
    return this.httpClient.post(`${environment.apiUrl}/pass-test/remark`, remark);
 }

  public getFinishTest(userId: BigInteger, testId: BigInteger ): Observable<any> {
    return this.httpClient.get(`${environment.apiUrl}/pass-test/${userId}/test/result/${testId}`);
  }

  public addAnswer(answer: Answer): Observable<any> {
      return this.httpClient.post(`${environment.apiUrl}/test/answer`, answer);
  }

}

