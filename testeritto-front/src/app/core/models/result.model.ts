import { Deserializable } from './deserializable.model';
import {Question} from './question.model';
import {Reply} from './reply.model';


export class Result implements Deserializable {
  date: Date;
  score: number;
  status: string;
  testId: BigInteger;
  replies: Map<string, Array<string>>;
  userId: BigInteger;
  categoryId: BigInteger;



    deserialize(input: any): this {
        return Object.assign(this, input);
    }
}
