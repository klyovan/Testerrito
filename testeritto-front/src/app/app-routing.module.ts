import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { ProfileComponent } from './profile/profile.component';
import { GroupComponent } from './group/group.component';
import { ResultComponent } from './result/result.component';
import {PassTestComponent} from './pass-test/pass-test.component';
import {RegisterComponent} from "./register/register.component";


const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'profile', component: ProfileComponent },
  { path: 'group/:groupId', component: GroupComponent },
  { path: 'group/result/:resultId', component: ResultComponent},
  { path: 'group/:groupId/users', component: GroupComponent },
  { path: 'pass-test/:userId/test/:id', component: PassTestComponent},
  { path: 'registration', component: RegisterComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
