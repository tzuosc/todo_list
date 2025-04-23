import { LogIn, LogOut, Settings, SquareChartGantt, Star, Sun } from "lucide-react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import {
    Sidebar,
    SidebarContent, SidebarFooter,
    SidebarHeader,
    SidebarMenu,
    SidebarMenuButton,
    SidebarMenuItem,
} from "@/components/ui/sidebar.tsx";
import { cn } from "@/utils";
import { InfoSwitch } from "@/components/widgets/sidebar/info-switcher.tsx";
import { AddList } from "@/components/widgets/addList";
import { useEffect, useState } from "react";
import { changeTodoListCategory, deleteByListId, getAllTodoLists } from "@/api/todolist";
import {
    ContextMenu,
    ContextMenuContent,
    ContextMenuItem, ContextMenuTrigger,

} from "@/components/ui/context-menu.tsx";
import { toast } from "sonner";
import { logout } from "@/api/user";
import * as React from "react";
import { useAuthStore } from "@/storages/auth.ts";

// 静态项有图标
type StaticItem = {
    title: string;
    path: string;
    icon: React.ElementType;
};

// 动态项没有图标
type DynamicItem = {
    title: string; //category
    path: string; //
    /*icon:React.ElementType;*/
    id: number;   //list_id
};

// 联合类型
type NavItem = StaticItem | DynamicItem;
const staticNavItems =[
    {
        title: "important",
        icon : Sun,
        path : "/list/重要"
    },
    {
        title: "my day",
        icon : Star,
        path : "/list/我的一天"
    },{
        title: "plan",
        icon : SquareChartGantt,
        path : "/list/计划内"
    },
]

export default function AppSidebar(){
    const [lists, setLists] = useState<DynamicItem[]>([])
    const location = useLocation()
    const fetchLists = async () => {
        try {
            const res = await getAllTodoLists();
            if (res.code === 200 && res.data) {
                const fetchedLists = res.data.map((item) => ({
                    title: item.category,
                    path: `/list/${item.category}`,
                    id: item.id,
                }));
                setLists(fetchedLists);
            }
        } catch (err) {
            console.error("获取列表失败", err);
        }
    };
    const authStore =useAuthStore()
    const navigate = useNavigate()
    const infoItem = [
        {
            title : "settings",
            logo : Settings,

            url : "/account/settings"

        },
        {
            title: "login/register",
            logo: LogIn,

            url: "/account/login"
        },
        {
            title: "logout",
            logo: LogOut,
            onClick: async () => {
                try {
                    const res = await logout();
                    if (res.code === 200) {
                        authStore.clear()
                        /*localStorage.removeItem("token");
                        sessionStorage.removeItem("token");
                        toast.success("已成功登出！");
                        navigate("/account/login");*/
                    } else {
                        toast.error("登出失败：" + res.msg);
                    }
                } catch (error) {
                    toast.error("登出失败，请稍后再试");
                }
            }
        }

    ]
    useEffect(() => {
        fetchLists();
    }, []);
    const allNavItems :NavItem[]= [...staticNavItems, ...lists];
    return(
        <Sidebar
            className={cn(["w-1/5",
                "flex","flex-col",
                "items-center","justify-between"])}
            collapsible={"icon"}> {/* 当侧边栏处于折叠状态时，只显示图标（icon），不显示文字 */}

            <SidebarHeader className={cn(["h-1/3"])}>
                <InfoSwitch  items={infoItem}/> {/*user's chart*/}
            </SidebarHeader>

            <SidebarContent>
                <SidebarMenu className={cn(["flex","justify-center","px-8"])}>
                    {allNavItems
                        .filter(Boolean)    // 过滤掉 null 或 undefined
                        .map((item)=>{
                        const isActive =location.pathname === item.path
                        const isStatic = "icon" in item;
                        const Icon = isStatic ? item.icon : null;
                        const category = item.title; // 从 path 中提取 id 或 category
                        return(
                            <SidebarMenuItem key={item.path}>
                                <ContextMenu>
                                    <ContextMenuTrigger>
                                        <SidebarMenuButton asChild isActive={isActive} tooltip={item.title}>
                                            <Link to={item.path}>
                                                {Icon ? (
                                                    <Icon className="w-5 h-5" />
                                                ) : (
                                                    <span className="w-5 h-5 inline-block" />
                                                )}
                                                <span>{item.title}</span>
                                            </Link>
                                        </SidebarMenuButton>
                                    </ContextMenuTrigger>
                                    {!isStatic && (
                                        <ContextMenuContent>

                                            <ContextMenuItem
                                                onClick={async () => {
                                                    const confirmed = window.confirm(`确认删除 "${category}" 吗？`);
                                                    if (!confirmed) return;

                                                    try {
                                                        const res = await deleteByListId(Number(item.id)); // 需要你传入 id
                                                        if (res.code === 200) {
                                                            toast.success("列表已删除");  // 刷新列表
                                                            setLists((prev) => prev.filter((l) => l.title !== category));
                                                        } else {
                                                            toast.error("删除失败", { description: res.msg });
                                                        }
                                                    } catch (err) {
                                                        toast.error("删除失败");
                                                    }
                                                }}
                                            >删除</ContextMenuItem>
                                            <ContextMenuItem
                                                onClick={async () => {
                                                    const newCategory = prompt(`请输入新的列表名称（原名称为：${item.title}）`, item.title);
                                                    if (!newCategory || newCategory === item.title) return;

                                                    try {
                                                        const res = await changeTodoListCategory(item.id, newCategory);
                                                        if (res.code === 200) {
                                                            toast.success("修改成功");
                                                            setLists((prev) =>
                                                                prev.map((l) =>
                                                                    l.id === item.id ? { ...l, title: newCategory, path: `/list/${newCategory}` } : l
                                                                )
                                                            );
                                                        } else {
                                                            toast.error("修改失败", { description: res.msg });
                                                        }
                                                    } catch (err) {
                                                        toast.error("修改失败");
                                                    }
                                                }}
                                            >修改</ContextMenuItem>

                                        </ContextMenuContent>
                                            )}
                                </ContextMenu>

                            </SidebarMenuItem>
                        )
                    })}
                </SidebarMenu>
            </SidebarContent>

            <SidebarFooter className={cn(["h-1/3","justify-center"])}>
                    <AddList onAddSuccess={()=>fetchLists()}/>
            </SidebarFooter>

        </Sidebar>
    )
}