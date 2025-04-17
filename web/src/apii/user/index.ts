import { User } from "@/models/user";
import { WebResponse } from "@/types";
import { alova } from "@/utils/alova";


export interface GetUserRequest {}
export async function getUsers(request: GetUserRequest) {
    return alova.Get<WebResponse<Array<User>>>("/users", {
        params: request,
    });
}