import {Answer} from './answer.model';


export class Reply {

  Id: BigInteger;
  resultId: BigInteger;
  replyList: Array<Answer>;
}
