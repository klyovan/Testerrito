import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { FormControl, Validators } from '@angular/forms';
import { GroupService } from '../core/api/group.service';
import { Group } from '../core/models/group.model';
import { Answer } from '../core/models/answer.model';
import { TestService } from '../core/api/test.service';

@Component({
  selector: 'app-create-group-form',
  templateUrl: './create-group-form.component.html',
  styleUrls: ['./create-group-form.component.css']
})
export class CreateGroupFormComponent implements OnInit {
  error: String;
  answers: Array<Answer>
  name = new FormControl(this.data.groupName, [Validators.required, Validators.minLength(3), Validators.maxLength(30), Validators.pattern('^([A-Za-z0-9]+((\\s){0,1}[A-Za-z0-9]*(\\s){0,1})*)([A-Za-z0-9]+)$')]);
  link = new FormControl(this.data.link);
  answersReady: Boolean = false;
  constructor(public dialogRef: MatDialogRef<CreateGroupFormComponent>,
              @Inject(MAT_DIALOG_DATA) public data,
              private groupService: GroupService,
              private testService: TestService) { }

  ngOnInit() {
    this.name.setErrors({ changeName: false})
    if(this.data.action == "changeQuestion" && this.data.question.typeQuestion != "OPEN") {
      this.testService.getAllAnswerInQuestion(this.data.question.id).subscribe(answers => {this.answers = answers; this.answersReady = true;});
    }
  }

  onYesCreate(): void {
    this.name.setValue(this.name.value.trim());
    this.name.setValue(this.name.value.replace('/\s+/', '_'));
    if(!this.name.invalid){
      this.groupService.group = new Group();
      this.groupService.group.name = this.name.value;
      this.groupService.group.link = "someNewLink";
      this.groupService.group.creatorUserId = this.data.creatorUserId;
      this.groupService.createGroup(this.groupService.group).subscribe(
        id => this.dialogRef.close(id),
        creatingError => { this.error = creatingError.error.message; this.name.setErrors({ changeName: true});} )
    }
  }

  onYesUpdate(): void {
    this.name.setValue(this.name.value.trim());
    this.name.setValue(this.name.value.replace('/\s+/', '_'));
    if(!this.name.invalid){
      this.groupService.group = new Group();
      this.groupService.group.id = this.data.groupId;
      this.groupService.group.name = this.name.value;
      this.groupService.group.creatorUserId = this.data.creatorUserId;
      this.groupService.updateGroup(this.groupService.group).subscribe(
        group => this.dialogRef.close(group),
        updatingError => {this.error = updatingError.error.message; this.name.setErrors({ changeName: true});} )
    }
  }

  onCopy(): void {
    document.addEventListener('copy', (e: ClipboardEvent) => {
      e.clipboardData.setData('text/plain', (this.data.link));
      e.preventDefault();
      document.removeEventListener('copy', null);
    });
    document.execCommand('copy');
  }

  resetLink() {
    this.link.setValue(this.data.link);
  }

  onNoClick(): void {
    this.dialogRef.close(false);
  }

  questionUpdate() {    
    this.answers.forEach(answer => {
      this.testService.updateAnswer(answer).subscribe()
    })    
    this.testService.updateQuestion(this.data.question).subscribe();
    this.dialogRef.close(this.data.question);
  }
}
