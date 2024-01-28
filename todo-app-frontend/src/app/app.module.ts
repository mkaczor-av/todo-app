import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { CommonModule } from "@angular/common";
import { TodoListComponent } from "./todo/component/todo-list/todo-list.component";
import { TableModule } from "primeng/table";
import { ButtonModule } from "primeng/button";
import { TodoService } from "./todo/service/todo.service";
import { HttpClientModule } from "@angular/common/http";
import { TodoFormComponent } from "./todo/component/todo-form/todo-form.component";
import { DynamicDialogModule } from "primeng/dynamicdialog";
import { TodoDatatableComponent } from "./todo/component/todo-list/todo-datatable/todo-datatable.component";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { InputTextModule } from "primeng/inputtext";
import { InputTextareaModule } from "primeng/inputtextarea";
import { ConfirmDialogModule } from "primeng/confirmdialog";
import { InputSwitchModule } from "primeng/inputswitch";
import { TodoDetailsComponent } from "./todo/component/todo-details/todo-details.component";
import { CardModule } from "primeng/card";

@NgModule({
  declarations: [
    AppComponent,
    TodoListComponent,
    TodoFormComponent,
    TodoDatatableComponent,
    TodoDetailsComponent,
  ],
  imports: [
    CommonModule,
    AppRoutingModule,
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    ButtonModule,
    TableModule,
    DynamicDialogModule,
    ReactiveFormsModule,
    InputTextModule,
    InputTextareaModule,
    ConfirmDialogModule,
    InputSwitchModule,
    FormsModule,
    CardModule,
  ],
  providers: [TodoService],
  bootstrap: [AppComponent]
})
export class AppModule { }
