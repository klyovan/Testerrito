import {AbstractControl, AsyncValidatorFn, ValidationErrors} from "@angular/forms";
import {Injectable} from "@angular/core";
import {debounceTime, map, switchMap, take} from "rxjs/operators";
import {Observable, of} from "rxjs";
import {UserService} from "../core/api/user.service";

function isEmptyInputValue(value: any): boolean {
    // we don't check for string here so it also works with arrays
    return value === null || value.length === 0;
}

@Injectable({
    providedIn: "root"
})
export class CustomEmailValidator{
    constructor(private userService: UserService) {}

    existingEmailValidator(initialEmail: string = ""): AsyncValidatorFn {
        return (
            control: AbstractControl
        ):
            | Promise<{ [key: string]: any } | null>
            | Observable<{ [key: string]: any } | null> => {
            if (isEmptyInputValue(control.value)) {
                return of(null);
            } else if (control.value === initialEmail) {
                return of(null);
            } else {
                return control.valueChanges.pipe(
                    debounceTime(500),
                    take(1),
                    switchMap(_ =>
                        this.userService
                            .getByEmail(control.value)
                            .pipe(
                                map(user =>
                                    user ? { existingEmail: { value: control.value } } : null
                                )
                            )
                    )
                );
            }
        };
    }




    existingPhoneValidator(initialPhone: string = ""): AsyncValidatorFn {
        return (
            control: AbstractControl
        ):
            | Promise<{ [key: string]: any } | null>
            | Observable<{ [key: string]: any } | null> => {
            if (isEmptyInputValue(control.value)) {
                return of(null);
            } else if (control.value === initialPhone) {
                return of(null);
            } else {
                return control.valueChanges.pipe(
                    debounceTime(500),
                    take(1),
                    switchMap(_ =>
                        this.userService
                            .getByPhone(control.value)
                            .pipe(
                                map(user =>
                                    user ? { existingPhone: { value: control.value } } : null
                                )
                            )
                    )
                );
            }
        };
    }
}
