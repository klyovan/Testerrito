import { NgModule } from '@angular/core';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { AuthService } from './api/auth.service';
import { TokenInterceptor } from './api/token.interceptor';

@NgModule({
  declarations: [],
  imports: [
    HttpClientModule
  ],
  providers: [
   {
      provide: HTTP_INTERCEPTORS,
     useClass: TokenInterceptor,
     multi: true
   },
    AuthService
  ]
})
export class CoreModule { }
