import { Component, inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip'

export const DIALOG_CLOSE_OK = 'Ok';
export const DIALOG_CLOSE_CANCEL = 'Cancel';

export interface DialogBoxData {
  data: {
    title: string;
    message?: string;
    cancelButton?: boolean;
    okButtonDisabled?: boolean;
  }
}

@Component({
  selector: 'dialog',
  templateUrl: './dialog.html',
  imports: [MatButtonModule, MatIconModule, MatTooltipModule]
})
export class Dialog {
  readonly dialogRef = inject(MatDialogRef<DialogBoxData>);
  readonly dialogData = inject(MAT_DIALOG_DATA);
  readonly closeOk = DIALOG_CLOSE_OK;
}
