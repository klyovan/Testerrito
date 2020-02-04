import {Deserializable} from './deserializable.model';

export class GradeCategory implements Deserializable {
  minScore: number;
  maxScore: number;
  meaning: string;
  categoryId: BigInteger;
  testId: BigInteger;
  categoryName: string;
  id: BigInteger;


  deserialize(input: any): this {
    return Object.assign(this, input);
  }
}
