import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
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
import { MatProgressSpinnerModule, MatTableModule, MatTabsModule, MatFormFieldModule, MatInputModule, MatPaginatorModule, MatTableDataSource, MatSortModule, MatSelect, MatSelectModule  } from '@angular/material';
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { CreateGroupFormComponent } from './create-group-form/create-group-form.component';
import { TestService } from './core/api/test.service';
import { AuthGuard } from './guard/auth.guard';
import { NoauthGuard } from './guard/noauth.guard';
import { InterceptorService } from './core/api/interceptor.service';
import { ErrorPageComponent } from './error-page/error-page.component';
import {MatCheckboxModule} from '@angular/material';
import {MatRadioModule} from '@angular/material/radio';
import {MatButtonModule} from '@angular/material/button';
import {ModalRemarkComponent} from './modal-remark/modal-remark.component';
import {MatTooltipModule} from '@angular/material/tooltip';
import {MatIconModule} from '@angular/material/icon';
import {MatExpansionModule} from '@angular/material/expansion';
import {MatListModule} from '@angular/material/list';
import {MatDividerModule} from '@angular/material/divider';
import {ChartsModule} from 'ng2-charts';
import { InvitationComponent } from './invitation/invitation.component';
import { TruncateModule } from '@yellowspot/ng-truncate';
import {RegisterComponent} from "./register/register.component";
import { TestComponent } from './test/test.component';
import { CreateTestComponent } from './create-test/create-test.component';
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
        CreateGroupFormComponent,
        ErrorPageComponent,
        ModalRemarkComponent,
        InvitationComponent,
        RegisterComponent,
        CreateTestComponent,
        TestComponent
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
        MatRadioModule,
        MatCheckboxModule,
        MatTooltipModule,
        MatIconModule,
        MatExpansionModule,
        MatListModule,
        MatDividerModule,
        ChartsModule,
        MatInputModule,
        MatTableModule,
        MatPaginatorModule,
        MatSortModule,
        MatTabsModule,
        MatProgressSpinnerModule,
        MatSelectModule,
        TruncateModule
    ],
    exports: [
        MatFormFieldModule
    ],
    providers: [UserService,
        GroupService,
        ResultService,
        TestService,
        PassTestService,
        DatePipe,
        MatDialog,
        MatTableDataSource,
        AuthGuard,
        NoauthGuard,
        {
            provide: HTTP_INTERCEPTORS,
            useClass: InterceptorService,
            multi: true
        }
    ],
    bootstrap: [AppComponent],
    entryComponents: [ConfirmDeleteComponent,ModalRemarkComponent, CreateGroupFormComponent]
})
export class AppModule { }
