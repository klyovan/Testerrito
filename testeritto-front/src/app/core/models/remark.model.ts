import { Deserializable } from './deserializable.model';

export class Remark implements Deserializable {
    id: BigInteger;
    text: String;
    userSenderId: BigInteger;
    userRecipientId: BigInteger;
    questionId: BigInteger;
    questionText: String;
    limit: Number = 20;
    testName: String;
    viewed: Boolean;

    deserialize(input: any): this {
        return Object.assign(this, input);
    }
}