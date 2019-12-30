import { Component, OnInit, AfterViewInit, OnChanges } from '@angular/core';
import { User } from '../core/models/user.model';
import { UserService } from '../core/api/user.service';
import { Observable } from 'rxjs';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../core/api/auth.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  public user = new User();
  profileForm: FormGroup;
  accountForm: FormGroup;
  isShowBtnsProfile = false;
  isShowBtnsAccount = false;

  constructor(private formBuilder: FormBuilder,
              private userService: UserService,
              private router: Router,
              private authService: AuthService) {
  }

  ngOnInit() {
    this.buildFormProfile();
    this.buildFormAccount();
    this.userService.getUser()
      .subscribe((user: User) => {
      this.user.id = user.id;
      this.user.email = user.email;
      this.user.lastName = user.lastName;
      this.user.firstName = user.firstName;
      this.user.phone = user.phone;
      this.user.password = user.password;
      this.user.groups = user.groups;
      this.user.createdGroups = user.createdGroups;
      this.user.results = user.results;
      this.profileForm.setValue({
        lastName: this.user.lastName,
        firstName: this.user.firstName,
        phone: this.user.phone
      });
      this.accountForm.patchValue({
        email: this.user.email
      });
    });
    this.profileForm.disable();
    this.accountForm.disable();
  }

  onEditProfile() {
    this.profileForm.enable();
    this.isShowBtnsProfile = true;
  }

  onCancelProfile() {
    this.profileForm.disable();
    this.isShowBtnsProfile = false;
    this.profileForm.get('lastName').setValue(this.user.lastName);
    this.profileForm.get('firstName').setValue(this.user.firstName);
    this.profileForm.get('phone').setValue(this.user.phone);
  }

  private buildFormProfile() {
    this.profileForm = this.formBuilder.group({
      // tslint:disable-next-line: max-line-length
      lastName: [ null, [Validators.required]],
      // tslint:disable-next-line: max-line-length
      firstName: [ null, [Validators.required]],
      phone: [ null, [Validators.required]]
    });
  }

  onEditAccount() {
    this.accountForm.enable();
    this.isShowBtnsAccount = true;
  }

  onCancelAccount() {
    this.accountForm.disable();
    this.isShowBtnsAccount = false;
    this.accountForm.get('email').setValue(this.user.email);
    this.accountForm.get('passwordNew').reset();
    this.accountForm.get('passwordConfirm').reset();
  }

  private buildFormAccount() {
    this.accountForm = this.formBuilder.group({
      email: [ null, [Validators.required, Validators.email, Validators.maxLength(40)]],
      passwordNew: [ null ],
      passwordConfirm: [ null ],
    });
  }

  onSubmitProfile() {
    const updatedUser = new User();
    const formValue = this.profileForm.value;
    updatedUser.id = this.user.id;
    updatedUser.firstName = formValue.firstName;
    updatedUser.lastName = formValue.lastName;
    updatedUser.phone = formValue.phone;
    this.userService.updateUser(updatedUser).subscribe(() => this.onSuccess());
    this.isShowBtnsProfile = false;
  }

  onSubmitAccount() {
    const updatedUser = new User();
    const formValue = this.accountForm.value;
    updatedUser.id = this.user.id;
    updatedUser.email = formValue.email;
    updatedUser.password = formValue.passwordNew;
    this.userService.updateUserCredentials(updatedUser).subscribe(() => this.onSuccess());
    this.isShowBtnsAccount = false;
  }

  private onSuccess() {
    this.router.navigate(['/profile']);
  }

}

