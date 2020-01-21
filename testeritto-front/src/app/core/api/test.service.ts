import { Injectable } from "@angular/core";
import { HttpClient } from '@angular/common/http';
import { GradeCategory } from '../models/gradecategory.model';
import { environment } from 'src/environments/environment';
import { map, tap } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { Question } from '../models/question.model';
import { Answer } from '../models/answer.model';

@Injectable()
export class TestService {
    constructor(private httpClient: HttpClient) {
        
    }     

    getTestGradeCategories(id: BigInteger): Observable<GradeCategory[]> {
        return this.httpClient.get<GradeCategory[]>(`${environment.apiUrl}/test/category/grade/test/`+id).pipe(
            map(grades => {
                return grades.map(grade => {
                    return new GradeCategory().deserialize(grade);
                })
            })
        )
    }

    getTestQuestions(id: BigInteger): Observable<Question[]> {
        return this.httpClient.get<Question[]>(`${environment.apiUrl}/test/question/test/`+id).pipe(
            map(questions => {
                return questions.map(question => {
                    return new Question().deserialize(question);
                })
            })
        )
    }

    getAllAnswerInQuestion(id: BigInteger): Observable<Answer[]> {
        return this.httpClient.get<Answer[]>(`${environment.apiUrl}/test/answer/question/`+id).pipe(
            map(answers => {
                return answers.map(answer => {
                    return new Answer().deserialize(answer);
                })
            })
        )
    }

    deleteTest(id: BigInteger): Observable<{}> {
        return this.httpClient.delete(`${environment.apiUrl}/test/`+id);
    }
}