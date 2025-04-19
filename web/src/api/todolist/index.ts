import { WebResponse } from "@/types";
import { alova } from "@/utils/alova.ts";

export interface GetAllListResponse {
    id: number;
    category: string;
    tasks: number[];
}
export async function getAllTodoLists() {
    return alova.Get<WebResponse<GetAllListResponse[]>>("/list");
}

export interface CreateTodoListParams {
    category: string;
}
export async function createTodoList(category: string) {
    return alova.Put<WebResponse<string>>("/list",{
        params: { category },
    });
}

export interface UpdateTodoListCategoryParams {
    id: number;
    newCategory: string;
}
export async function deleteTodoList(id: number) {
    return alova.Delete<WebResponse<string>>(`/list/${id}`);
}
export async function changeTodoListCategory(id: number, newCategory: string) {
    return alova.Patch<WebResponse<string>>(`/list/change_category/${id}`,{
        params: { newCategory },
    });
}
