
export class Category {
    deserialize(input: any): any {
      return Object.assign(this, input);
    }
    id: BigInteger;
    nameCategory: string;
  
  }
