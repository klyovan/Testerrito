import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {LoginComponent} from './login/login.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import {CoreModule} from './core/core.module';
import {HeaderComponent} from './header/header.component';
import {ProfileComponent} from './profile/profile.component';
import {UserService} from './core/api/user.service';
import {GroupComponent} from './group/group.component';
import {GroupService} from './core/api/group.service';
import {ResultComponent} from './result/result.component';
import {ResultService} from './core/api/result.service';
import {PassTestComponent} from './pass-test/pass-test.component';
import {PassTestService} from './core/api/pass-test.service';
import {DatePipe} from '@angular/common';
import {GroupusersComponent} from './groupusers/groupusers.component';
import {RemarkComponent} from './remark/remark.component';
import {GrouplistComponent} from './grouplist/grouplist.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatCheckboxModule} from '@angular/material';
import {MatRadioModule} from '@angular/material/radio';
import {MatDialogModule} from '@angular/material/dialog';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {ModalRemarkComponent} from './modal-remark/modal-remark.component';
import {MatTooltipModule} from '@angular/material/tooltip';
import {MatIconModule} from '@angular/material/icon';
import {MatExpansionModule} from '@angular/material/expansion';
import {MatListModule} from '@angular/material/list';

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
        ModalRemarkComponent
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
        MatProgressSpinnerModule,
        MatButtonModule,
        MatInputModule,
        MatDialogModule,
        MatRadioModule,
        MatCheckboxModule,
        MatTooltipModule,
        MatIconModule,
        MatExpansionModule,
        MatListModule
    ],
    providers: [UserService, GroupService, ResultService, PassTestService, DatePipe],
    bootstrap: [AppComponent],
    entryComponents: [ModalRemarkComponent]
})
export class AppModule {
}
