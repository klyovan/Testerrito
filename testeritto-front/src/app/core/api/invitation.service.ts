import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {environment} from '../../../environments/environment';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class InvitationService {

  constructor(private httpClient: HttpClient) { }


  public inviteUser(link: string): Observable<any> {
    return this.httpClient.get(`${environment.apiUrl}/group/invite/${link}`);
  }

  public isUserConsistInGroup(userId: BigInteger, groupId: BigInteger): Observable<any> {
    return this.httpClient.get(`${environment.apiUrl}/user/check/consist/${groupId}/${userId}`);
  }

}
