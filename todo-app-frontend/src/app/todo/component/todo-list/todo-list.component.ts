import { Component, OnInit } from "@angular/core";
import { CreateTodoDto, TodoDto, TodoPagedResponse, UpdateTodoDto } from "../../model/todo.model";
import { TodoService } from "../../service/todo.service";
import { DialogService } from "primeng/dynamicdialog";
import {
  TodoFormComponent,
  TodoFormComponentInputData,
  TodoFormComponentOutputData
} from "../todo-form/todo-form.component";
import { ConfirmationService } from "primeng/api";
import { Router } from "@angular/router";
import { debounceTime, Subject } from "rxjs";

@Component({
  selector: 'todo-list',
  templateUrl: 'todo-list.component.html',
  styleUrls: ['todo-list.component.css'],
  providers: [DialogService]
})
export class TodoListComponent implements OnInit {
  todos: TodoDto[] = []
  currentPage: number = 0;
  totalElements: number = 0
  numberOfRows: number = 0
  loading: boolean = true

  nameFilter: string = ""

  private nameFilterSubject = new Subject<string>();

  constructor(
    private todoService: TodoService,
    private dialogService: DialogService,
    private router: Router,
    private confirmationService: ConfirmationService
  ) {
    this.nameFilterSubject.pipe(debounceTime(1000)).subscribe(() => {
      this.onNameFilterChange();
    });
  }

  ngOnInit(): void {
    this.loadTodos()
  }

  createTodo(): void {
    this.dialogService.open(
      TodoFormComponent,
      this.createConfig('Create todo')
    ).onClose.subscribe((todoData: TodoFormComponentOutputData) => {
      if (todoData == null) {
        return
      }

      this.todoService.create(
        {
          name: todoData.name,
          description: todoData.description,
        } as CreateTodoDto
      ).subscribe(() => {
        this.loadTodos()
      })
    });
  }

  editTodo(todo: TodoDto): void {
    this.dialogService.open(
      TodoFormComponent,
      this.createConfig('Update todo', todo)
    ).onClose.subscribe((todoData: TodoFormComponentOutputData) => {
      if (todoData == null) {
        return
      }

      this.todoService.update(
        todoData.uuid,
        {
          name: todoData.name,
          description: todoData.description,
          completed: todoData.completed,
        } as UpdateTodoDto
      ).subscribe(() => {
        this.loadTodos()
      })
    });
  }

  deleteTodo(todoUuid: string): void {
    this.confirmationService.confirm({
      message: "Do you want to delete this record?",
      header: "Delete Confirmation",
      icon: "pi pi-info-circle",
      rejectButtonStyleClass: "p-button-text",
      accept: () => {
        this.todoService.delete(todoUuid).subscribe(() => this.loadTodos())
      }
    })
  }

  pageChange($event: number): void {
    this.currentPage = $event
    this.loadTodos()
  }

  private loadTodos(): void {
    this.todoService.findAll(this.currentPage, this.nameFilter).subscribe((todoPagedResponse: TodoPagedResponse) => {
      this.todos = todoPagedResponse.content
      this.totalElements = todoPagedResponse.totalElements
      this.numberOfRows = todoPagedResponse.size
      this.loading = false
    })
  }

  private createConfig(header: string, todo: TodoDto | null = null) {
    return {
      header: header,
      height: '50%',
      width: '30%',
      data: {
        uuid: todo?.uuid,
        name: todo?.name,
        description: todo?.description,
        completed: todo?.completed,
      } as TodoFormComponentInputData
    };
  }

  onCompletedSwitch(todo: TodoDto): void {
    this.todoService.update(
      todo.uuid,
      {
        name: todo.name,
        description: todo.description,
        completed: todo.completed,
      } as UpdateTodoDto
    ).subscribe(() => {
      this.loadTodos()
    })
  }

  todoDetails(todoUuid: string): void {
    this.router.navigate(['/todos', todoUuid])
  }

  onNameInputChange() {
    this.nameFilterSubject.next(this.nameFilter);
  }

  onNameFilterChange(): void {
    this.loadTodos()
  }
}
