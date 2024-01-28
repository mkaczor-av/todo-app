import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TodoListComponent } from "./todo/component/todo-list/todo-list.component";
import { TodoDetailsComponent } from "./todo/component/todo-details/todo-details.component";

const routes: Routes = [
  { path: '', component: TodoListComponent },
  { path: 'todos/:uuid', component: TodoDetailsComponent }
];

@NgModule({
  declarations: [],
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
