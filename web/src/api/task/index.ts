import { alova } from "@/utils/alova.ts";
import { WebResponse } from "@/types";
import { TodoList } from "@/models/todolist.ts";
import { Task } from "@/models/task.ts";

/* 增 */
export interface CreateTaskRequest {
    name: string; //名称
    description?: string;
    status?: boolean;
    deadline?: number;
    todoList?:TodoList //category
}
export async function createTask(data: CreateTaskRequest) {
    return alova.Post<WebResponse<string>>("/task", data);
}


/* 查  删 */
export interface GetTaskResponse {
    id: number;
}
export async function getTask(id: number) {
    return alova.Get<WebResponse<GetTaskResponse>>(`/task/${id}`);
}
export async function deleteTask(id: number) {
    return alova.Delete<WebResponse<string>>(`/task/${id}`);
}

/* 改 */
export interface UpdateTaskRequest {
    name?: string;
    description?: string;
    status?: boolean;
    deadline?: number;
    category?:string;
}
export async function updateTask(id: number, data: UpdateTaskRequest) {
    return alova.Patch<WebResponse<null>>(`/task/${id}`, data);
}

export interface Task[]{

}
export async function fetchByListId(listId:number){
    return alova.Get<WebResponse<Task[]>>(`list/${listId}/tasks`)
}