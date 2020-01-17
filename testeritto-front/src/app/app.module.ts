import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { CoreModule } from './core/core.module';
import { HeaderComponent } from './header/header.component';
import { ProfileComponent } from './profile/profile.component';
import { UserService } from './core/api/user.service';
import { GroupComponent } from './group/group.component';
import { GroupService } from './core/api/group.service';
import { ResultComponent } from './result/result.component';
import { ResultService } from './core/api/result.service';
import { PassTestComponent } from './pass-test/pass-test.component';
import { PassTestService } from './core/api/pass-test.service';
import { DatePipe } from '@angular/common';
import { GroupusersComponent } from './groupusers/groupusers.component';
import { RemarkComponent } from './remark/remark.component';
import { GrouplistComponent } from './grouplist/grouplist.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ConfirmDeleteComponent } from './confirm-delete/confirm-delete.component';
import { MatProgressSpinnerModule, MatTableModule, MatTabsModule, MatFormFieldModule, MatInputModule, MatPaginatorModule, MatTableDataSource, MatSortModule  } from '@angular/material';
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { CreateGroupFormComponent } from './create-group-form/create-group-form.component';
import { TestService } from './core/api/test.service';
@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HeaderComponent,
    ProfileComponent,
    GroupComponent,
    ResultComponent,
    PassTestComponent,
    GroupusersComponent,
    RemarkComponent,
    GrouplistComponent,
    ConfirmDeleteComponent,
    CreateGroupFormComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    CoreModule,
    NgbModule,
    BrowserAnimationsModule,
    MatFormFieldModule,
    MatDialogModule,
    MatInputModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatTabsModule,
    MatProgressSpinnerModule
  ],
  exports: [
    MatFormFieldModule
  ],
  providers: [UserService, GroupService, ResultService, TestService, PassTestService, DatePipe, MatDialog, MatTableDataSource],
  bootstrap: [AppComponent],
  entryComponents: [ConfirmDeleteComponent, CreateGroupFormComponent]
})
export class AppModule { }
