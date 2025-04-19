import { TodoList } from "@/models/todolist.ts";

export interface Task{
    id?:number,
    name?:string, //
    description?:string,
    deadline?:number,
    status?:boolean,
    todoList?: TodoList,

}