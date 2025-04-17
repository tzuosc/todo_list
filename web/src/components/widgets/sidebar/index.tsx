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

const navItems =[
    {
        title: "111",
        icon : Sun,
        path : "/"
    },
    {
        title: "222",
        icon : Star,
        path : "/importance"
    },{
        title: "333",
        icon : SquareChartGantt,
        path : "/plan"
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
    const location = useLocation()
    return(
        <Sidebar collapsible={"icon"}>


            <SidebarHeader>

                <InfoSwitch items={infoItem}/>
            </SidebarHeader>


            <SidebarContent>
                <SidebarMenu>
                    {navItems.map((item)=>{
                        const isActive =location.pathname === item.path
                        return(
                            /* */
                            <SidebarMenuItem key={item.path}>
                                <SidebarMenuButton asChild isActive={isActive} tooltip={item.title}>
                                    <Link to={item.path}>
                                        <item.icon className={cn(["w-5 h-5"])}/>
                                        <span>{item.title}</span>
                                    </Link>
                                </SidebarMenuButton>
                            </SidebarMenuItem>
                        )
                    })}
                </SidebarMenu>
            </SidebarContent>
            <SidebarFooter>
                <div className={cn([" p-2"])}>
                    <div className={cn(["rounded-lg bg-muted p-2"])}>
                        <p>Sidebar footer</p>
                    </div>
                </div>
            </SidebarFooter>

        </Sidebar>
    )
}