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

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HeaderComponent,
    ProfileComponent,
    GroupComponent,
    ResultComponent,
    PassTestComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    CoreModule
  ],
  providers: [UserService, GroupService, ResultService, PassTestService],
  bootstrap: [AppComponent]
})
export class AppModule { }
