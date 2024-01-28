import { Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { TodoService } from "../../service/todo.service";
import {TodoDto} from "../../model/todo.model";


@Component({
  selector: 'todo-details',
  templateUrl: 'todo-details.component.html',
  styleUrls: ['todo-details.component.css'],
})
export class TodoDetailsComponent implements OnInit {

  todoDto: TodoDto | undefined;

  constructor(private activatedRoute: ActivatedRoute, private todoService: TodoService) {
  }

  ngOnInit(): void {
    const todoUuid = this.activatedRoute.snapshot.params['uuid'];

    this.todoService.find(todoUuid).subscribe((todoDto: TodoDto) => this.todoDto = todoDto);
  }
}
