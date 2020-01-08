import { Component, OnInit } from '@angular/core';
import { User } from './core/models/user.model';
import { UserService } from './core/api/user.service';
import { AuthService } from './core/api/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'testeritto-front';
  get isLoggedIn() {
    return this.authService.isLoggedIn;
  }

  constructor(private authService: AuthService) {}
}
