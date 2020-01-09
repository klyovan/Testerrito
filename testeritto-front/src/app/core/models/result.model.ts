import { Deserializable } from './deserializable.model';
import {Question} from './question.model';
import {Reply} from './reply.model';
import { map, tap } from 'rxjs/operators';

export class Result implements Deserializable {
  id: BigInteger;
  date: Date;
  score: number;
  status: string;
  testId: BigInteger;
  replies: Map<String, Array<String>>;
  userId: BigInteger;
  categoryId: BigInteger;

    deserialize(input: any): this {
        return Object.assign(this, input);              
    }
}
