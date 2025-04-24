import { WebResponse } from "@/types";
import { alova } from "@/utils/alova.ts";
import { TodoList } from "@/models/todolist.ts";

export interface GetAllListResponse {
    id: number;
    category: string;
    tasks: number[];
}



export async function createTodoList(category: string) {
    return alova.Put<WebResponse<string>>(`/list/${category}`);
}

export async function getAllTodoLists() {
    return alova.Get<WebResponse<GetAllListResponse[]>>("/list");
}
export interface ChangeTodoListCategory {
    id: number
    newCategory: string
}
export async function changeTodoListCategory(request: ChangeTodoListCategory) {
    return alova.Patch<WebResponse<TodoList>>(
        `/list/change_category/${request.id}`,
        { params: { newCategory: request.newCategory } }
    )
}


export async function fetchByListId(id:number){
    return alova.Get<WebResponse<TodoList>>(`/list/${id}`)
}

export async function deleteByListId(id: number) {
    return alova.Delete<WebResponse<string>>(`/list/${id}`);
}
/*
* 这里返回的 WebResponse<string> 表示：code：状态码，表示操作是否成功。
*
* msg：如果删除成功，通常是 "删除成功"，如果失败，则可能返回错误信息。
*
* data：通常为空或包含成功/错误消息。
*
* ts：时间戳，表示操作的时间。
* */