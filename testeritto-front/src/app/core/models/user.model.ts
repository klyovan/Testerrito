import { Group } from './group.model';
import { Result } from './result.model';
import { Deserializable } from './deserializable.model';

export class User implements Deserializable {
    id: BigInteger;
    lastName: string;
    firstName: string;
    email: string;
    password: string;
    phone: string;
    groups: Array<Group> ;
    results: Array<Result> ;
    createdGroups: Array<Group>;
  updatedUser: any;

    deserialize(input: any): this {
        Object.assign(this, input);
        this.groups = input.groups.map(group => new Group().deserialize(group));
        this.createdGroups = input.createdGroups.map(createdGroup => new Group().deserialize(createdGroup));
        this.results = input.results.map(result => new Result().deserialize(result));
        return this;
      }
}
