import {AbstractControl, ValidationErrors} from "@angular/forms";

export class UsernameValidator {
    static cannotContainNonAlphabetChar(control: AbstractControl) : ValidationErrors | null {
       // let str :String = control;
        let pattern =new RegExp("[A-Za-z]");
        for(let i = 0; i < (control.value as string).length; i++ ){
            if(!(control.value as string)[i].match(pattern)){

                return {cannotContainNonAlphabetChar: true}
            }
        }

        // if((control.value as string).indexOf(' ') >= 0){
        //     return {cannotContainSpace: true}
        // }

        return null;
    }
}
