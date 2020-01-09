import { Deserializable } from './deserializable.model';

export class Remark implements Deserializable {
    id: BigInteger;
    text: String;
    userSenderId: BigInteger;
    userRecipientId: BigInteger;
    questionId: BigInteger;

    deserialize(input: any): this {
        return Object.assign(this, input);
    }
}