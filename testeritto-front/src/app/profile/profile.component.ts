import { Component, OnInit, AfterViewInit, OnChanges } from '@angular/core';
import { User } from '../core/models/user.model';
import { UserService } from '../core/api/user.service';
import { Observable } from 'rxjs';
import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../core/api/auth.service';
import { ConfirmDeleteComponent } from '../confirm-delete/confirm-delete.component';
import { MatDialog } from '@angular/material';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  public user = new User();
  profileForm: FormGroup;
  accountForm: FormGroup;
  deleteForm: FormGroup;
  isShowBtnsProfile = false;
  isShowBtnsAccount = false;
  isSubmittedAccount = false;
  isSubmittedProfile = false;
  isDelettedAccount = false;
  isEqualsPswrds = true;

  constructor(private formBuilder: FormBuilder,
              private userService: UserService,
              private authService: AuthService,
              public dialog: MatDialog) {
  }

  ngOnInit() {
    this.buildFormProfile();
    this.buildFormAccount();
    this.buildFormDelete();
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
      lastName: [ null, [Validators.required, Validators.pattern('^([a-zA-Z -.]{2,40})$')]],
      firstName: [ null, [Validators.required, Validators.pattern('^([a-zA-Z -.]{2,40})$')]],
      phone: [ null, [Validators.required, Validators.pattern('[\+0-9\(\) -]{7,18}')]]
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
    this.accountForm.get('passwordOld').reset();
    this.accountForm.get('passwordNew').reset();
    this.accountForm.get('passwordConfirm').reset();
    this.isEqualsPswrds = true;
  }

  private buildFormAccount() {
    this.accountForm = this.formBuilder.group({
      email: [ null, [Validators.required, Validators.email, Validators.maxLength(40)]],
      passwordNew: [ null, [Validators.pattern('^[^а-яА-Я]{8,}$')] ],
      passwordConfirm: [ null, [Validators.pattern('^[^а-яА-Я]{8,}$')] ],
      passwordOld: [ null, [Validators.pattern('^[^а-яА-Я]{8,}$')] ]
    });
  }

  private buildFormDelete() {
    this.deleteForm = this.formBuilder.group({
      password: [null, [Validators.required, Validators.pattern('^[^а-яА-Я]{8,}$')] ]
    });
  }

  onSubmitProfile() {
    this.isSubmittedProfile = true;
    console.log(this.profileForm.controls.phone.hasError('pattern'));
    if (this.profileForm.invalid) {
      return;
    }
    const updatedUser = new User();
    const formValue = this.profileForm.value;
    updatedUser.id = this.user.id;
    updatedUser.firstName = formValue.firstName;
    updatedUser.lastName = formValue.lastName;
    updatedUser.phone = formValue.phone;
    this.userService.updateUser(updatedUser).subscribe(() => this.onSuccess(), () => this.onFailed());
    this.isShowBtnsProfile = false;
    this.profileForm.disable();
  }

  onSubmitAccount() {
    this.isSubmittedAccount = true;
    if (this.accountForm.invalid) {
      return;
    }
    const updatedUser = new User();
    const userWithOldPswd = new User();
    const formValue = this.accountForm.value;
    if (formValue.passwordNew === formValue.passwordConfirm) {
      updatedUser.id = this.user.id;
      updatedUser.email = formValue.email;
      updatedUser.password = formValue.passwordNew;
      userWithOldPswd.id = this.user.id;
      userWithOldPswd.email = formValue.email;
      userWithOldPswd.password = formValue.passwordOld;
      this.userService.updateUserCredentials(userWithOldPswd, updatedUser).subscribe(() => this.onSuccess(), () => this.onFailed());
      this.isShowBtnsAccount = false;
      this.accountForm.disable();
    } else {
      this.isEqualsPswrds = false;
    }
  }

  onDeleteAccount() {
    this.isDelettedAccount = true;
    if (this.deleteForm.invalid) {
      return;
    }
    const formValue = this.deleteForm.value;
    const dialogRef = this.dialog.open(ConfirmDeleteComponent, {
      data: {title: "DELETE ACCOUNT", text: "Are You sure that you want to delete your account?" }
    });

    dialogRef.afterClosed().subscribe(result => {
      if(result) {
        this.userService.deleteUser(formValue.password).subscribe(() => this.onSuccessDel(), () => this.onFailedDel());
      }
    });    
  }

  onFailedDel() {
    console.log("Failed to delete");
  }

  onSuccessDel() {
    console.log("Success to delete");
    this.authService.logout();
  }

  private onSuccess() {
    this.isSubmittedAccount = false;
    this.ngOnInit();
  }

  private onFailed() {
    console.log("SMTH INCORRECT");
  }
}

