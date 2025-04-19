import { alova } from "@/utils/alova.ts";
import { WebResponse } from "@/types";

/* 增 */
export interface CreateTaskRequest {
    name: string; // 任务名称（必须上传）
    description?: string;
    status?: boolean;
    deadline?: number;


}
export async function createTask(data: CreateTaskRequest) {
    return alova.Post<WebResponse<string>>("/task", data);
    /*
    * code msg data
    * */
}


/* 查  删 */
export interface GetTaskResponse {
    id: number;
    name: string;
    description: string;
    deadline: number;
    status: boolean;
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
    todoListId?:number
}
export async function updateTask(id: number, data: UpdateTaskRequest) {
    return alova.Patch<WebResponse<null>>(`/task/${id}`, data);
}


