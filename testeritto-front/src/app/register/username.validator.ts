import {AbstractControl, ValidationErrors} from "@angular/forms";

export class UsernameValidator {
    static cannotContainNonAlphabetChar(control: AbstractControl) : ValidationErrors | null {
       // let str :String = control;
        let pattern =new RegExp("[A-Za-z\\s]");

        // if((control.value as string)[0].match( new RegExp("\\s"))){
        //     return {cannotContainNonAlphabet: "can not start with space"}
        // }

        for(let i = 0; i < (control.value as string).length; i++ ){

            if(!(control.value as string)[i].match(pattern)){

                return {cannotContainNonAlphabetChar: true}
            }
        }
        return null;
    }
    static cannotStartWithSpace(control: AbstractControl) : ValidationErrors | null {
     let pattern =new RegExp("^ +.*"); //

       if( (control.value as string).match(pattern)) {
           return {cannotStartWithSpace: true}
        }

        return null;
    }

    static cannotFinishWithSpace(control: AbstractControl) : ValidationErrors | null {
        let pattern =new RegExp("\w* $");

        if((control.value as string).match(pattern)){
            return {cannotFinishWithSpace: true}
        }

        return null;
    }


    static canContainOnlyNumbers(control: AbstractControl) : ValidationErrors | null {
        // let str :String = control;
        let pattern =new RegExp("[0-9]");
        for(let i = 0; i < (control.value as string).length; i++ ){
            if(!(control.value as string)[i].match(pattern)){

                return {canContainOnlyNumbers: true}

            }
        }
       return null;

    }


}
