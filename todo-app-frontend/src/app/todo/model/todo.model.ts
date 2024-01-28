export interface TodoPagedResponse {
  content: TodoDto[],
  size: number,
  totalElements: number,
}

export interface TodoDto {
  uuid: string,
  name: string,
  description: string,
  completed: boolean,
}

export interface CreateTodoDto {
  name: string,
  description: string,
}

export interface UpdateTodoDto {
  name: string,
  description: string,
  completed: boolean,
}

