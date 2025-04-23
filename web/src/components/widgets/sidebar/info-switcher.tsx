"use client"

import * as React from "react"
import { useNavigate } from "react-router-dom"
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuLabel,
    DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu.tsx"
import { SidebarMenu, SidebarMenuButton, SidebarMenuItem, useSidebar } from "@/components/ui/sidebar.tsx"
import { Avatar } from "@/components/ui/avatar.tsx";
import { cn } from "@/utils";
import { useAuthStore } from "@/storages/auth.ts";


// Example data structure
type InfoItem = {
    title: string
    logo: React.ElementType
    url?: string
    onClick?:()=>void
}

export function InfoSwitch(
    {
        items,
        defaultSelected = 0,
    }: {
    items: InfoItem[]
    defaultSelected?: number
}) {
    /*const {user} =*/
    const { isMobile } = useSidebar()
    const navigate = useNavigate()
    const [activeItem, setActiveItem] = React.useState<InfoItem | null>(items.length > 0 ? items[defaultSelected] : null)

    if (!activeItem) {
        return null
    }

    const handleItemClick = (item: InfoItem) => {
        if (item.onClick) {
            item.onClick(); // If there is an onClick handler, call it
        }else {
            setActiveItem(item);
            if (item.url) {
                navigate(item.url); // Normal navigation
            }
        }
    }
    const user = useAuthStore((state)=>state.user)
    return (
        <SidebarMenu className={cn([ "w-full","h-full","justify-center"])}>
            <SidebarMenuItem className={cn(["flex",
                "flex","lg:flex-col","flex-row",
                "w-full",
                "justify-center","items-center"])}>

                {/* 如果没有登录则显示未登录 登录状态存储在AuthStore里(/storages/auth) */}
                {user?(
                    < Avatar src={"https://avatars.githubusercontent.com/u/149759599?v=4"}
                             className={cn([
                                 "lg:w-60","lg:h-60",
                                 "w-[20vw]","h-[20vw]",
                             ])}
                             fallback={"CN"} />
                ):(<span>没有登录</span>)}

                <DropdownMenu>
                    <DropdownMenuTrigger className={"h-auto"} asChild>
                        <SidebarMenuButton
                            size="lg"
                            className={cn(["mt-4"
                                /*"data-[state=open]:bg-sidebar-accent data-[state=open]:text-sidebar-accent-foreground",*/
                            ])}
                        >
                            {/*leading-tight 是 Tailwind CSS 中的一个实用类，用来控制 行高（line-height）*/}
                            <div className={cn([
                                "flex-1",
                                "text-center ",
                                "lg:text-2xl",
                                "leading-tight","font-sans","font-medium",
                                "w-full"
                            ])}>
                                {user ? `${user.username}'s chat` : "Guest"}
                            </div>

                        </SidebarMenuButton>
                    </DropdownMenuTrigger>
                    <DropdownMenuContent
                        className="w-[--radix-dropdown-menu-trigger-width] min-w-56 rounded-lg"
                        align="start"
                        side={isMobile ? "bottom" : "right"}
                        sideOffset={4}
                    >
                        <DropdownMenuLabel className="text-xs text-muted-foreground">Menu</DropdownMenuLabel>
                        {items.map((item) => (
                            <DropdownMenuItem key={item.title} onClick={() => handleItemClick(item)} className="gap-2 p-2">
                                <div className="flex size-6 items-center justify-center rounded-sm border">
                                    <item.logo className="size-4 shrink-0" />
                                </div>
                                {item.title}
                            </DropdownMenuItem>
                        ))}

                    </DropdownMenuContent>

                </DropdownMenu>
            </SidebarMenuItem>
        </SidebarMenu>
    )
}
