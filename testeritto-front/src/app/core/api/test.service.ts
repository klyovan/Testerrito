import { Injectable } from "@angular/core";
import { HttpClient } from '@angular/common/http';
import { GradeCategory } from '../models/gradecategory.model';
import { environment } from 'src/environments/environment';
import { map, tap } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { Question } from '../models/question.model';
import { Answer } from '../models/answer.model';
import { Test } from '../models/test.model';
import { Category } from '../models/category.model';

@Injectable()
export class TestService {
    constructor(private httpClient: HttpClient) {
        
    }     


    public getTest(id: BigInteger): Observable<any> {
        return this.httpClient.get(`${environment.apiUrl}/test/${id}`);
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
        return this.httpClient.get<Question[]>(`${environment.apiUrl}/test/question/test/` + id).pipe(
            map(questions => {
                return questions.map(question => {
                    return new Question().deserialize(question);
                })
            })
        )
    }

    getCategoryById(id: BigInteger): Observable<Category>{
        return this.httpClient.get<Category>(`${environment.apiUrl}/test/category/` + id).pipe(
            map((category: Category) => {             
                return new Category().deserialize(category);
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




    updateTest(test: Test): Observable<Test> {
        return this.httpClient.put<Test>(`${environment.apiUrl}/test/update`,test).pipe(
            map(test => {
                return new Test().deserialize(test);
            })
        )
    }
    updateCategory(category: Category): Observable<Category> {
        return this.httpClient.put<Category>(`${environment.apiUrl}/test/category`,category).pipe(
            map(category => {
                return new Category().deserialize(category);
            })
        )
    }
    updateGradeCategory(grCat: GradeCategory): Observable<GradeCategory> {
        return this.httpClient.put<GradeCategory>(`${environment.apiUrl}/test/category/grade`,grCat).pipe(
            map(grCat => {
                return new GradeCategory().deserialize(grCat);
            })
        )
    }
    updateQuestion(question: Question): Observable<Question> {
        return this.httpClient.put<Question>(`${environment.apiUrl}/test/question`,question).pipe(
            map(question => {
                return new Question().deserialize(question);
            })
        )
    }

    updateAnswer(answer: Answer): Observable<Answer> {
        return this.httpClient.put<Answer>(`${environment.apiUrl}/test/answer`,answer).pipe(
            map(answer => {
                return new Answer().deserialize(answer);
            })
        )
    }

   



    createTest(test:Test):Observable<BigInteger>{
        return this.httpClient.post<BigInteger>(`${environment.apiUrl}/test/create`, test);
    }

    createQuestion(question:Question): Observable<BigInteger>{
        return this.httpClient.post<BigInteger>(`${environment.apiUrl}/test/question/create`, question);
    }

    createAnswer(answer:Answer): Observable<BigInteger>{
        return this.httpClient.post<BigInteger>(`${environment.apiUrl}/test/answer/create`, answer);
    }
    createCategory(category:Category):Observable<BigInteger>{
        return this.httpClient.post<BigInteger>(`${environment.apiUrl}/test/category/create`, category);
    }
    createGradeCategory(gradecategory:GradeCategory):Observable<BigInteger>{
        return this.httpClient.post<BigInteger>(`${environment.apiUrl}/test/category/grade/create`, gradecategory);
    }






    deleteTest(id: BigInteger): Observable<{}> {
        return this.httpClient.delete(`${environment.apiUrl}/test/`+ id);
    }
    deleteCategory(id: BigInteger): Observable<{}> {
        return this.httpClient.delete(`${environment.apiUrl}/test/category/`+ id);
    }

    deleteGradeCategory(id: BigInteger): Observable<{}> {
        return this.httpClient.delete(`${environment.apiUrl}/test/category/grade/`+ id);
    }
    deleteQuestion(id: BigInteger): Observable<{}> {
        return this.httpClient.delete(`${environment.apiUrl}/test/question/`+ id);
    }
    deleteAnswer(id: BigInteger): Observable<{}> {
        return this.httpClient.delete(`${environment.apiUrl}/test/answer/`+ id);
    }
}
