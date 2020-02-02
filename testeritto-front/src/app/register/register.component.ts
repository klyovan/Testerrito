import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Component, OnInit} from "@angular/core";
import {catchError, first} from "rxjs/operators";
import {UserService} from "../core/api/user.service";
import {Router} from "@angular/router";
import {UsernameValidator} from "./username.validator";
import {MustMatch} from "./match.validator";
import {throwError} from "rxjs";
import {CustomEmailValidator} from "./email.validator";
import {AlertService} from "../core/api/alert.service";


@Component({templateUrl: 'register.component.html'})
export class RegisterComponent implements OnInit {
    registerForm: FormGroup;

    loading = false;
    submitted = false;
    // router: any;
    // private formBuilder: any;

    constructor(private formBuilder: FormBuilder,
                private userService: UserService,
                private emailValidator: CustomEmailValidator,
                private alertService: AlertService,
                private router: Router) {
    }


    // convenience getter for easy access to form fields
    get f() {
        return this.registerForm.controls;
    }

    ngOnInit() {
        this.registerForm = this.formBuilder.group({
            firstName: ['', [Validators.required, Validators.maxLength(20),
                             UsernameValidator.cannotStartWithSpace,  UsernameValidator.cannotContainNonAlphabetChar,
                             UsernameValidator.cannotFinishWithSpace]],
            lastName: ['',  [Validators.required, UsernameValidator.cannotContainNonAlphabetChar,
                             Validators.maxLength(20), UsernameValidator.cannotStartWithSpace,  UsernameValidator.cannotFinishWithSpace]],
            email: ['',     [Validators.required,  Validators.email, Validators.pattern('[a-z_.0-9]+@[a-z_]+\\.[a-z]{2,3}')], [this.emailValidator.existingEmailValidator()]],
            password: ['',  [Validators.required, Validators.minLength(6), Validators.maxLength(30)]],
            phone: ['',     [Validators.required, Validators.minLength(9), Validators.maxLength(9),
                          UsernameValidator.canContainOnlyNumbers], [this.emailValidator.existingPhoneValidator()]], // имена
            confirmPassword: ['', Validators.required]
        }, {
            validator: MustMatch('password', 'confirmPassword')
        });
    }


    onSubmit() {
        this.submitted = true;



        // stop here if form is invalid
        if (this.registerForm.invalid) {
            return;
        }

        //    this.loading = true;
        this.userService.register(this.registerForm.value)
            .pipe(first())
            .subscribe(
                data => {
                   this.alertService.success('Registration successful', true);
                   // alert('User Registered successfully!!');
                    this.router.navigate(['/login']);
                },
                error => {
                    this.alertService.error(error);
                }
                );

    }


}
