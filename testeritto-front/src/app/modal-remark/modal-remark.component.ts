import {Component, Inject, OnInit} from '@angular/core';
import {MatDialogRef, MAT_DIALOG_DATA} from '@angular/material';
import {FormControl, Validators} from '@angular/forms';


export interface DialogData {
  remarkText: string;
  remarkQuestion: string;
}

@Component({
  selector: 'app-modal-remark',
  templateUrl: './modal-remark.component.html',
  styleUrls: ['./modal-remark.component.css']
})
export class ModalRemarkComponent implements OnInit {
  remarkForm = new FormControl('', [Validators.required]);
  closeMessage = 'Your complaint has been sent to admin';


  constructor(
      public dialogRef: MatDialogRef<ModalRemarkComponent>,
      @Inject(MAT_DIALOG_DATA) public data: DialogData) {}
  ngOnInit() {
  }

  onNoClick(): void {

    this.dialogRef.close();

  }

  getErrorMessage() {
    return this.remarkForm.hasError('required') ? 'You must enter a value' :
            'Enter more than 10 characters!';
  }

}
