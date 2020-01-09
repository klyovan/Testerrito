import { Deserializable } from './deserializable.model';
import {Answer} from './answer.model';

export class Question implements Deserializable {

  id: BigInteger;
  textQuestion: string;
  typeQuestion: string;
  testId: BigInteger;
  answers: Array<Answer>;
  categoryId: BigInteger;

    deserialize(input: any): this {
        return Object.assign(this, input);
    }
}
