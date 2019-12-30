import { Component, OnInit, Input, TemplateRef } from '@angular/core';
import { AuthService } from 'src/app/core/api/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  @Input() navBarTmpl: TemplateRef<any>;

  constructor(private authService: AuthService,
              private router: Router) { }

  ngOnInit() {
  }

  logout() {
    this.authService.logout();
  }

  get isLoggedIn(): boolean {
    return this.authService.isLoggedIn;
  }
}
