import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Result } from '../models/result.model';
import { environment } from 'src/environments/environment';
import { map, tap } from 'rxjs/operators';

@Injectable()
export class ResultService {
    results: Array<Result>;
    constructor(private httpClient: HttpClient) {
        
    }   
}
