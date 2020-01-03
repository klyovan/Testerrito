import { Component, OnInit } from '@angular/core';
import { Group } from '../core/models/group.model';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { UserService } from '../core/api/user.service';
import { User } from '../core/models/user.model';
import { GroupService } from '../core/api/group.service';
import { Result } from '../core/models/result.model';

@Component({
  selector: 'app-group',
  templateUrl: './group.component.html',
  styleUrls: ['./group.component.css']
})
export class GroupComponent implements OnInit {
  group: Group;
  user: User;
  results: Array<Result>;
  finalResult: Map<Date,String>;
  loading: Boolean = false;
  testsLoaded: Boolean = false;
  showResult: Boolean = false;
  infoIfNoResult: String;
  selectedTest: BigInteger;

  constructor(private route: ActivatedRoute,
              private userService: UserService,
              private groupService: GroupService,
              private router: Router) {
    this.group = new Group;
    route.params.subscribe(params=>this.group.id=params['groupId']);  
   }

  ngOnInit() { 
    this.userService.getUser().subscribe((user: User) => {
      this.user = user; 
      user.groups.forEach(element => {
        if(element.id == this.group.id){
          this.loading = true;
          this.groupService.getUserResultsForTest(this.user.id).subscribe(data => {
            this.user.results = data;
          });
          return;
        }
        this.router.navigate(['']);
      });
    });  
    this.groupService.getGroup(this.group.id).subscribe((group: Group) => {
      this.group = group;   
      this.testsLoaded = true;
    }); 
  }

  seeResultsOnTest(id: BigInteger) {
    this.selectedTest = id;
    this.user.results.filter(element => element.testId = this.selectedTest)
    this.showResult = true;
  }

  copyLinkToClipboard() {
    document.addEventListener('copy', (e: ClipboardEvent) => {
      e.clipboardData.setData('text/plain', (this.group.link));
      e.preventDefault();
      document.removeEventListener('copy', null);
    });
    document.execCommand('copy');
  }

  // passTest(id: BigInteger) {
  //   var now = new Date();
  //   var nowDate = (now.getFullYear() + "-" + now.getMonth() + "-" + now.getDate()).toString();
  //   var checkResult = this.user.results.find( element => 
  //     element.date.toString() == nowDate.toString() && element.testId == id
  //   )
  //   if(checkResult == null)      
  //     this.router.navigateByUrl('/pass-test/'+id);
  //   else alert("You already passed test today. Come back tomorrow :)" + checkResult.id);
  // }

}
