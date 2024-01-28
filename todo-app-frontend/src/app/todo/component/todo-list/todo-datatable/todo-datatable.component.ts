import { Component, EventEmitter, Input, Output } from "@angular/core";
import { ColumnItem } from "../../../../common/model/column-item.model";
import { TodoDto } from "../../../model/todo.model";
import { TableLazyLoadEvent } from "primeng/table";
import { InputSwitchChangeEvent } from "primeng/inputswitch";

@Component({
  selector: 'todo-datatable',
  templateUrl: 'todo-datatable.component.html',
  styleUrls: ['todo-datatable.component.css'],
})
export class TodoDatatableComponent {
  @Input() loading: boolean | undefined;
  @Input() totalRecords: number = 0;
  @Input() numberOfRows: number = 0;
  @Input() todos: TodoDto[] = []
  @Output() editTodo: EventEmitter<TodoDto> = new EventEmitter<TodoDto>()
  @Output() deleteTodo: EventEmitter<string> = new EventEmitter<string>()
  @Output() pageChange: EventEmitter<number> = new EventEmitter<number>()
  @Output() completedSwitch: EventEmitter<TodoDto> = new EventEmitter<TodoDto>()
  @Output() todoDetails: EventEmitter<string> = new EventEmitter<string>()

  cols: ColumnItem[] = [
    { field: 'name', header: 'Name' },
    { field: 'description', header: 'Description' },
  ]

  onGoToDetails(todoUuid: string): void {
    this.todoDetails.emit(todoUuid);
  }

  onEditTodo(todo: TodoDto): void {
    this.editTodo.emit(todo)
  }

  onDeleteTodo(todoUuid: string): void {
    this.deleteTodo.emit(todoUuid)
  }

  loadTodos($event: TableLazyLoadEvent) {
    const selectedPage: number = ($event.first ?? 0) / 10

    this.pageChange.emit(selectedPage)
  }

  onCompletedSwitch($event: InputSwitchChangeEvent, todo: TodoDto) {
    this.completedSwitch.emit({
      uuid: todo.uuid,
      name: todo.name,
      description: todo.description,
      completed: $event.checked,
    });
  }
}
