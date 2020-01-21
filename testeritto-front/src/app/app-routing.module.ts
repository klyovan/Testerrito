import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { ProfileComponent } from './profile/profile.component';
import { GroupComponent } from './group/group.component';
import { ResultComponent } from './result/result.component';
import { PassTestComponent } from './pass-test/pass-test.component';
import { GroupusersComponent } from './groupusers/groupusers.component';
import { RemarkComponent } from './remark/remark.component';
import { GrouplistComponent } from './grouplist/grouplist.component';
import { AuthGuard } from './guard/auth.guard';
import { NoauthGuard } from './guard/noauth.guard';
import { ErrorPageComponent } from './error-page/error-page.component';


const routes: Routes = [
  { path: 'login', component: LoginComponent, canActivate: [NoauthGuard] },
  { path: 'profile', component: ProfileComponent, canActivate: [AuthGuard] },
  { path: 'group', component: GrouplistComponent, canActivate: [AuthGuard] },
  { path: 'group/:groupId', component: GroupComponent, canActivate: [AuthGuard] },
  { path: 'group/:groupId/results', component: ResultComponent, canActivate: [AuthGuard] },
  { path: 'group/:groupId/users', component: GroupusersComponent, canActivate: [AuthGuard] },
  { path: 'group/:groupId/remarks', component: RemarkComponent, canActivate: [AuthGuard] },
  { path: 'pass-test/:userId/test/:id', component: PassTestComponent, canActivate: [AuthGuard] },
  { path: 'error-page/:status', component: ErrorPageComponent },
  { path: '**', redirectTo: '/error-page/404', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
