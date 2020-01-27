import { Component, OnInit, QueryList, ViewChildren } from '@angular/core';
import { UserService } from '../core/api/user.service';
import { User } from '../core/models/user.model';
import { Router } from '@angular/router';
import { GroupService } from '../core/api/group.service';
import {MatDialog } from '@angular/material/dialog';
import { ConfirmDeleteComponent } from '../confirm-delete/confirm-delete.component';
import { CreateGroupFormComponent } from '../create-group-form/create-group-form.component';
import { MatTableDataSource, MatSort, MatPaginator } from '@angular/material';
import { Group } from '../core/models/group.model';

@Component({
  selector: 'app-grouplist',
  templateUrl: './grouplist.component.html',
  styleUrls: ['./grouplist.component.css']
})
export class GrouplistComponent implements OnInit {
  user: User;
  @ViewChildren(MatPaginator) paginator = new QueryList<MatPaginator>();
  @ViewChildren(MatSort) sort = new QueryList<MatSort>();
  createdGroupsDataSourse = new MatTableDataSource<Group>();  
  consistGroupsDataSourse = new MatTableDataSource<Group>();  
  displayedCreatedGroupsColumns: string[] = ['name', 'show', 'update', 'delete'];
  displayedConsistGroupsColumns: string[] = ['name', 'show', 'delete'];
  loading: Boolean = false;
  isConstistEmpty: Boolean = false;
  isCreatedGroupsEmpty: Boolean = false;
  color = 'primary';
  mode = 'indeterminate';
  value = 50;
  constructor(private userService: UserService,
              private groupService: GroupService,
              public dialog: MatDialog,
              private router: Router) {}

  ngOnInit() {    
    this.userService.getUser().subscribe(data => {
      this.user = data;
      this.user.createdGroups.forEach(createdGroup => {
        var index = this.user.groups.findIndex(group => group.id == createdGroup.id);
        if(index != -1)
          this.user.groups.splice(index, 1);        
      })
      this.loading = true;      
      this.changeCreatedGroupsDataSourse();
      this.changeConsistGroupsDataSourse();
    });    
  }

  changeCreatedGroupsDataSourse() {
    this.createdGroupsDataSourse = new MatTableDataSource<Group>(this.user.createdGroups);
    if(this.createdGroupsDataSourse.data.length == 0)
      this.isCreatedGroupsEmpty = true;   
    this.createdGroupsDataSourse.paginator = this.paginator.toArray()[0];     
    this.createdGroupsDataSourse.sort = this.sort.toArray()[0];   
  }

  changeConsistGroupsDataSourse() {
    this.consistGroupsDataSourse = new MatTableDataSource<Group>(this.user.groups);   
    if(this.consistGroupsDataSourse.data.length == 0)
      this.isConstistEmpty = true;   
    this.consistGroupsDataSourse.paginator = this.paginator.toArray()[1];     
    this.consistGroupsDataSourse.sort = this.sort.toArray()[1];  
  }

  goToGroup(id: BigInteger) {
      this.router.navigateByUrl('/group/'+id);
  }

  createGroup() {
    const dialogRef = this.dialog.open(CreateGroupFormComponent, {
      data: {action: "create", groupName: '', creatorUserId: this.user.id },
      width: "450px"
    });

    dialogRef.afterClosed().subscribe(result => {
      if(result) 
        this.groupService.getGroup(result).subscribe(group => {
          this.user.createdGroups.push(group);
          this.changeCreatedGroupsDataSourse();
        })
    })
  }

  updateGroup(id: BigInteger) {
    const dialogRef = this.dialog.open(CreateGroupFormComponent, {
      data: {action: "update", 
             groupName: this.user.createdGroups.find(group => group.id == id).name, 
             groupId: id, 
             creatorUserId: this.user.id },
      width: "450px"
    });

    dialogRef.afterClosed().subscribe(result => {
      if(result) {
        this.user.createdGroups.find(group => group.id == result.id).name = result.name;
        this.changeCreatedGroupsDataSourse();
      }
    })
  }

  deleteGroup(id: BigInteger) { 
    const dialogRef = this.dialog.open(ConfirmDeleteComponent, {
      data: {title: "DELETE GROUP", text: "Are You sure that you want to delete this group?" }
    });

    dialogRef.afterClosed().subscribe(result => {
      if(result) {
        this.groupService.deleteGroup(id).subscribe();
        var index = this.user.createdGroups.findIndex(group => group.id == id);
        if(index != -1) {
          this.user.createdGroups.splice(index, 1);
          this.changeCreatedGroupsDataSourse();
        }          
      }
    });
  }

  exitFromGroup(id: BigInteger) {
    const dialogRef = this.dialog.open(ConfirmDeleteComponent, {
      data: {title: "EXIT FROM GROUP", text: "Are You sure that you want exit from this group?" }
    });

    dialogRef.afterClosed().subscribe(result => {
      if(result) {
        this.groupService.exitFromGroup(this.user.id, id).subscribe();
        var index = this.user.groups.findIndex(group => group.id == id);
        if(index != -1) {
          this.user.groups.splice(index, 1); 
          this.changeConsistGroupsDataSourse();
        }
      }
    });
    
  }
}
