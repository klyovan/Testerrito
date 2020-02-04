import { Deserializable } from './deserializable.model';

export class Answer implements Deserializable {
    id: BigInteger;
    textAnswer: string;
    score: number;
    questionId: BigInteger;
    nextQuestionId: BigInteger;
    replyId: BigInteger;
    questionText:string;
  

  deserialize(input: any): this {
    return Object.assign(this, input);
  }
}
  