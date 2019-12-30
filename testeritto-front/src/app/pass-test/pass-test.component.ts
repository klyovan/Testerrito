import {Component, Input, OnInit} from '@angular/core';
import {PassTestService} from '../core/api/pass-test.service';
import {UserService} from '../core/api/user.service';
import {ActivatedRoute, Router} from '@angular/router';
import {Result} from '../core/models/result.model';
import {User} from '../core/models/user.model';
import {Observable} from 'rxjs';




@Component({
  selector: 'app-pass-test',
  templateUrl: './pass-test.component.html',
  styleUrls: ['./pass-test.component.css']
})
export class PassTestComponent implements OnInit {
  id: BigInteger;
  test: any;
  userId: BigInteger;
  index = 0;
  user: User;




  iterateIndex() {
    if (this.index < 9) {
    return this.index++; } else {this.index = 0; }
  }


  constructor(private route: ActivatedRoute, private router: Router,
              private passTestService: PassTestService, private userService: UserService) { }

  ngOnInit() {
    this.userService.getUser().subscribe((user: User) => this.userId = user.id );
    this.id = this.route.snapshot.params.id;
    this.userId = this.route.snapshot.params.userId;
    this.passTestService.getTest(this.userId, this.id).subscribe((data) => this.test = data );
   // this.replies = this.passTestService.getReplies().subscribe((data) => {this.result  = data; });
    // this.questions = this.passTestService.getTest(25, this.result);

  }




}
