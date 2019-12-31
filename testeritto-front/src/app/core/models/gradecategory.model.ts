import {Deserializable} from './deserializable.model';

export class GradeCategory implements Deserializable {
  deserialize(input: any): this {
    return Object.assign(this, input);
  }
}
