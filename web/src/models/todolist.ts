import type { Task } from "@/models/task";

export interface TodoList {
    id?: number;
    category?: string;
    tasks?: Task[];
}