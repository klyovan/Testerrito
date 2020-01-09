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


const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'profile', component: ProfileComponent },
  { path: 'group', component: GrouplistComponent },
  { path: 'group/:groupId', component: GroupComponent },
  { path: 'group/:groupId/results', component: ResultComponent },
  { path: 'group/:groupId/users', component: GroupusersComponent },
  { path: 'group/:groupId/remarks', component: RemarkComponent },
  { path: 'pass-test/:userId/test/:id', component: PassTestComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
