import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Component, OnInit} from "@angular/core";
import {first} from "rxjs/operators";
import {UserService} from "../core/api/user.service";
import {Router} from "@angular/router";
import {UsernameValidator} from "./username.validator";
import {MustMatch} from "./match.validator";


@Component({templateUrl: 'register.component.html'})
export class RegisterComponent implements OnInit {
    registerForm: FormGroup;
    loading = false;
    submitted = false;
   // router: any;
   // private formBuilder: any;

    constructor( private formBuilder: FormBuilder,
                 private userService: UserService,
                 private router: Router) {
    }


    // convenience getter for easy access to form fields
    get f() { return this.registerForm.controls; }

    ngOnInit() {
        this.registerForm = this.formBuilder.group({
            firstName: ['',[ Validators.required, UsernameValidator.cannotContainNonAlphabetChar, Validators.minLength(3)]],
            lastName: ['', [Validators.required, Validators.pattern(/[A-Za-z]+/), Validators.minLength(3)]],
            email: ['', Validators.required],
            password: ['', [Validators.required, Validators.minLength(6)]],
            phone:['',[Validators.required, Validators.minLength(9), Validators.maxLength(9)]],
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
                    //this.alertService.success('Registration successful', true);
                    this.router.navigate(['/login']);
                });

    }


}
