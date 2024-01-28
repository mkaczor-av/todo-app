import { Component, OnInit } from "@angular/core";
import { FormBuilder, Validators } from "@angular/forms";
import { DynamicDialogConfig, DynamicDialogRef } from "primeng/dynamicdialog";

@Component({
  selector: 'todo-form',
  templateUrl: 'todo-form.component.html',
  styleUrls: ['todo-form.component.css']
})
export class TodoFormComponent implements OnInit {
  todoForm: any;

  constructor(private config: DynamicDialogConfig, private dynamicDialogRef: DynamicDialogRef, private fb: FormBuilder) {}

  ngOnInit(): void {
    const config: TodoFormComponentInputData = this.config.data as TodoFormComponentInputData

    this.todoForm = this.fb.group({
      name: [config.name, Validators.required],
      description: [config.description],
    })
  }

  get todoFormControls(): any {
    return this.todoForm['controls']
  }

  save(): void {
    const config: TodoFormComponentInputData = this.config.data as TodoFormComponentInputData

    this.dynamicDialogRef.close({
      uuid: config.uuid,
      name: this.todoForm.value.name,
      description: this.todoForm.value.description,
      completed: config.completed,
    } as TodoFormComponentOutputData)
  }
}

export interface TodoFormComponentInputData {
  uuid: string,
  name: string,
  description: string,
  completed: boolean,
}

export interface TodoFormComponentOutputData {
  uuid: string,
  name: string,
  description: string,
  completed: boolean,
}
