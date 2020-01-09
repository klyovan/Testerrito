import { Component, OnInit } from '@angular/core';
import { UserService } from '../core/api/user.service';
import { User } from '../core/models/user.model';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { GroupService } from '../core/api/group.service';
import { Group } from '../core/models/group.model';

@Component({
  selector: 'app-grouplist',
  templateUrl: './grouplist.component.html',
  styleUrls: ['./grouplist.component.css']
})
export class GrouplistComponent implements OnInit {
  user: User;
  loading: Boolean = false;
  loadCreateForm: Boolean = false;
  createGroupForm: FormGroup;
  isSubmited: Boolean = false;
  error: string;
  constructor(private formBuilder: FormBuilder,
              private userService: UserService,
              private groupService: GroupService,
              private router: Router) { }

  ngOnInit() {
    this.userService.getUser().subscribe(data => {
      this.user = data;
      this.user.createdGroups.forEach(createdGroup => {
        var index = this.user.groups.findIndex(group => group.id == createdGroup.id);
        if(index != -1)
          this.user.groups.splice(index, 1);        
      })
      this.loading = true;
    });
  }

  private buildForm() {
    this.createGroupForm = this.formBuilder.group({
      GroupName: [ null, Validators.required ],
    });
  }

  onSubmit() {
    this.isSubmited = true;
    this.error = null;

    if (this.createGroupForm.invalid) {
      this.error = 'Invalid name';
      return;
    }

    const formValue = this.createGroupForm.value;
    this.groupService.group = new Group();
    this.groupService.group.name = formValue.GroupName;
    this.groupService.group.link = "someNewLink";
    this.groupService.group.creatorUserId = this.user.id;
    this.groupService.create(this.groupService.group)
      .subscribe((data) => {
        this.groupService.group.id=data;
        this.groupService.getGroup(data).subscribe(group => this.user.createdGroups.push(group));
        this.loadCreateForm = false;
      });
  }

  goToGroup(id: BigInteger) {
      this.router.navigateByUrl('/group/'+id);
  }

  createGroup() {
    this.loadCreateForm = true;
    this.buildForm();
  }

  deleteGroup(id: BigInteger) {
    this.groupService.deleteGroup(id).subscribe();
    var index = this.user.createdGroups.findIndex(group => group.id == id);
        if(index != -1)
          this.user.createdGroups.splice(index, 1); 
  }
}
