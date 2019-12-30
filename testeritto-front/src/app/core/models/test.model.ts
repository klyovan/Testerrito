import { Deserializable } from './deserializable.model';
import { User } from './user.model';
import { GradeCategory } from './gradecategory.model';
import { Question } from './question.model';

export class Test implements Deserializable{
    id: BigInteger;
    groupId: BigInteger;
    nameTest: String;
    creatorUserId: BigInteger;
    gradesCategory: Array<GradeCategory>;
    experts: Array<User>;
    questions: Array<Question>;
    
    deserialize(input: any): this {
       Object.assign(this, input);
            if(input.gradesCategory != null)
       this.gradesCategory = input.gradesCategory.map(gradesCategory1 => new User().deserialize(gradesCategory1));
       if(input.experts != null)
       this.experts = input.experts.map(expert => new User().deserialize(expert));
       if(input.questions != null)
       this.questions = input.questions.map(question => new User().deserialize(question));
       return this;
    }
}
