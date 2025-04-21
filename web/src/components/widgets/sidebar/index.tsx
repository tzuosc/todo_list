import { LogIn, Settings, SquareChartGantt, Star, Sun } from "lucide-react";
import { Link, useLocation } from "react-router-dom";
import {
    Sidebar,
    SidebarContent, SidebarFooter,
    SidebarHeader,
    SidebarMenu,
    SidebarMenuButton,
    SidebarMenuItem,
} from "@/components/ui/sidebar.tsx";
import { cn } from "@/utils";
import { InfoSwitch } from "@/components/info-switcher.tsx";
import { AddList } from "@/components/widgets/addList";
import { useEffect, useState } from "react";
import { getAllTodoLists } from "@/api/todolist";

// 静态项有图标
type StaticItem = {
    title: string;
    path: string;
    icon: React.ElementType;
};

// 动态项没有图标
type DynamicItem = {
    title: string;
    path: string;
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

    }
]

export default function AppSidebar(){
    const [lists, setLists] = useState<{ title: string; path: string }[]>([])
    const location = useLocation()
    useEffect(() => {
        async function fetchLists() {
            try {
                const res = await getAllTodoLists();
                if (res.code === 200 && res.data) {
                    const fetchedLists = res.data.map((item) => ({
                        title: item.category,
                        path: `/list/${item.category}`
                    }));
                    setLists(fetchedLists);
                }
            } catch (err) {
                console.error("获取列表失败", err);
            }
        }

        fetchLists();
    }, []);
    const allNavItems :NavItem[]= [...staticNavItems, ...lists];
    return(
        <Sidebar collapsible={"icon"}>


            <SidebarHeader>

                <InfoSwitch items={infoItem}/>
            </SidebarHeader>


            <SidebarContent>
                <SidebarMenu>
                    {allNavItems.map((item)=>{
                        const isActive =location.pathname === item.path
                        return(
                            /* */
                            <SidebarMenuItem key={item.path}>
                                <SidebarMenuButton asChild isActive={isActive} tooltip={item.title}>
                                    <Link to={item.path}>
                                        {"icon" in item && item.icon ? (
                                            <item.icon className={cn(["w-5 h-5"])} />
                                        ) : (
                                            <span className="w-5 h-5 inline-block" /> // 占位符
                                        )}
                                        <span>{item.title}</span>
                                    </Link>
                                </SidebarMenuButton>
                            </SidebarMenuItem>
                        )
                    })}
                </SidebarMenu>
            </SidebarContent>
            <SidebarFooter>
                <div className={cn([" "])}>
                    {/*<div className={cn(["rounded-lg bg-muted p-2"])}>
                        <p>Sidebar footer</p>
                    </div>*/}
                    <AddList/>
                </div>
            </SidebarFooter>

        </Sidebar>
    )
}