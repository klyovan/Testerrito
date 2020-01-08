import { User } from './user.model';
import { Test } from './test.model';
import { Deserializable } from './deserializable.model';

export class Group implements Deserializable {
    id: BigInteger;
    name: string;
    link: string;
    creatorUserId: BigInteger;
    users: Array<User>;
    tests: Array<Test>;

    deserialize(input: any): this {
        Object.assign(this, input);
        if(input.tests != null)
            this.tests = input.tests.map(test => new Test().deserialize(test));
        if(input.users != null)
            this.users = input.users.map(user => new User().deserialize(user));
        return this;
    }
}
