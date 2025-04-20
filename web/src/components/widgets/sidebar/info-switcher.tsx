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

// Example data structure
type InfoItem = {
    title: string
    logo: React.ElementType
    url: string
}

export function InfoSwitch({
                               items,
                               defaultSelected = 0,
                           }: {
    items: InfoItem[]
    defaultSelected?: number
}) {
    const { isMobile } = useSidebar()
    const navigate = useNavigate()
    const [activeItem, setActiveItem] = React.useState<InfoItem | null>(items.length > 0 ? items[defaultSelected] : null)

    if (!activeItem) {
        return null
    }

    const handleItemClick = (item: InfoItem) => {
        setActiveItem(item)
        navigate(item.url)
    }

    return (
        <SidebarMenu>
            <SidebarMenuItem>
                <DropdownMenu>
                    <DropdownMenuTrigger asChild>
                        <SidebarMenuButton
                            size="lg"
                            className="data-[state=open]:bg-sidebar-accent data-[state=open]:text-sidebar-accent-foreground"
                        >

                                < Avatar src={"https://avatars.githubusercontent.com/u/149759599?v=4"}
                                         className="size-12"
                                         fallback={"CN"}
                                />

                            <div className="grid flex-1 text-left text-sm leading-tight">
                                username's chat
                            </div>

                        </SidebarMenuButton>
                    </DropdownMenuTrigger>
                    <DropdownMenuContent
                        className="w-[--radix-dropdown-menu-trigger-width] min-w-56 rounded-lg"
                        align="start"
                        side={isMobile ? "bottom" : "right"}
                        sideOffset={4}
                    >
                        <DropdownMenuLabel className="text-xs text-muted-foreground">Navigation</DropdownMenuLabel>
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
