import { Injectable } from "@angular/core";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { CreateTodoDto, TodoDto, TodoPagedResponse, UpdateTodoDto } from "../model/todo.model";

@Injectable()
export class TodoService {

  private static readonly BACKEND_BASE_URL: string = "http://localhost:8080"
  private static readonly DEFAULT_PAGE_SIZE: number = 10

  constructor(private http: HttpClient) {}

  create(createTodoDto: CreateTodoDto): Observable<TodoDto> {
    return this.http.post<TodoDto>(`${TodoService.BACKEND_BASE_URL}/todos`, createTodoDto);
  }

  update(todoUuid: string, updateTodoDto: UpdateTodoDto): Observable<TodoDto> {
    return this.http.put<TodoDto>(`${TodoService.BACKEND_BASE_URL}/todos/${todoUuid}`, updateTodoDto);
  }

  delete(todoUuid: string): Observable<void> {
    return this.http.delete<void>(`${TodoService.BACKEND_BASE_URL}/todos/${todoUuid}`);
  }

  find(todoUuid: string): Observable<TodoDto> {
    return this.http.get<TodoDto>(`${TodoService.BACKEND_BASE_URL}/todos/${todoUuid}`);
  }

  findAll(page: number = 0, nameFilter: string = ''): Observable<TodoPagedResponse> {
    let queryParams: HttpParams = new HttpParams()

    queryParams = queryParams.append('page', page)
    queryParams = queryParams.append('size', TodoService.DEFAULT_PAGE_SIZE)

    if (nameFilter) {
      queryParams = queryParams.append('name', nameFilter)
    }

    return this.http.get<TodoPagedResponse>(
      `${TodoService.BACKEND_BASE_URL}/todos`,
      { params: queryParams }
    )
  }
}
