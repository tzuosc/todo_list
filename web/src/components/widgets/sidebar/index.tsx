import { useState, useEffect } from "react"
import { Link, useNavigate } from "react-router-dom"
import {
    Sidebar,
    SidebarHeader,
    SidebarContent,
    SidebarFooter,
} from "@/components/ui/sidebar.tsx"
import {
    ContextMenu,
    ContextMenuTrigger,
    ContextMenuContent,
    ContextMenuItem,
} from "@/components/ui/context-menu.tsx"
import { Dialog, DialogContent } from "@/components/ui/dialog.tsx"
import { Button } from "@/components/ui/button.tsx"
import { EditIcon, TrashIcon, LogIn, LogOut, Settings } from "lucide-react"
import { cn } from "@/utils"
import { AddList } from "@/components/widgets/sidebar/edit-list/add-list"
import { InfoSwitch } from "@/components/widgets/sidebar/info-switcher.tsx"
import { getAllTodoLists } from "@/api/todolist"
import { logout } from "@/api/user"
import { toast } from "sonner"
import { useAuthStore } from "@/storages/auth.ts"
import { DeleteListDialog } from "@/components/widgets/sidebar/edit-list/delete-list-dialog.tsx"
import { UpdateListDialog } from "@/components/widgets/sidebar/edit-list/update-list-dialog.tsx"

type NavItem = { title: string; path: string; id: number }

// 单个列表项，含右键菜单和弹窗
function NavListItem({ item, onRefresh }: { item: NavItem; onRefresh: () => void }) {


    const [showDelete, setShowDelete] = useState(false)
    const [showUpdate, setShowUpdate] = useState(false)

    const handleClose = () => {
        setShowDelete(false)
        setShowUpdate(false)
        onRefresh()
    }

    return (
        <>
            <ContextMenu>
                <ContextMenuTrigger>
                    <Button
                        asChild
                        variant={"ghost"}
                        className={cn(["w-full justify-start px-6 py-2",
                            "text-lg",
                            "font-medium",

                        ])}
                    >
                        <Link to={item.path}>{item.title}</Link>
                    </Button>
                </ContextMenuTrigger>
                <ContextMenuContent>

                    <ContextMenuItem onClick={() => setShowUpdate(true)}>
                        <EditIcon className="w-4 h-4 mr-2" /> 修改
                    </ContextMenuItem>

                    <ContextMenuItem onClick={() => setShowDelete(true)}>
                        <TrashIcon className="w-4 h-4 mr-2" /> 删除
                    </ContextMenuItem>

                </ContextMenuContent>
            </ContextMenu>

            <Dialog open={showDelete} onOpenChange={setShowDelete}>
                <DialogContent className={cn(["flex","items-center","justify-center"])}>
                    <DeleteListDialog
                        listId={item.id}
                        category={item.title}
                        onClose={handleClose}
                    />
                </DialogContent>
            </Dialog>

            <Dialog open={showUpdate} onOpenChange={setShowUpdate}>
                <DialogContent>
                    <UpdateListDialog
                        listId={item.id}
                        category={item.title}
                        onClose={handleClose}
                    />
                </DialogContent>
            </Dialog>
        </>
    )
}

export default function AppSidebar() {
    const [lists, setLists] = useState<NavItem[]>([])
    const authStore = useAuthStore()
    const navigate = useNavigate()

    const fetchLists = async () => {
        const res = await getAllTodoLists()
        if (res.code === 200 && res.data) {
            setLists(res.data.map((i) => ({
                id: i.id,
                title: i.category,
                path: `/list/${i.category}`,
            })))
        }
    }

    useEffect(() => { fetchLists() }, [])

    const infoItems = [
        { title: "个人设置", logo: Settings, url: "/account/settings" },
        { title: "登录/注册", logo: LogIn, url: "/account/login" },
        {
            title: "登出",
            logo: LogOut,
            onClick: async () => {
                const res = await logout()
                if (res.code === 200) {
                    authStore.clear()
                    toast.success("已成功登出！")
                    navigate("/account/login")
                } else {
                    toast.error("登出失败：" + res.msg)
                }
            },
        },
    ]

    return (
        <Sidebar className={cn("w-1/5 flex flex-col justify-between")} collapsible="icon">
            <SidebarHeader className="h-1/3">
                <InfoSwitch items={infoItems} />

            </SidebarHeader>

            <SidebarContent className={cn(["overflow-auto","px-15"])}>

                <nav className={cn(["space-y-1 "])}>
                    {lists.map((item) => (
                        <NavListItem key={item.id} item={item} onRefresh={fetchLists} />
                    ))}
                </nav>
            </SidebarContent>

            <SidebarFooter className="h-1/3 flex items-center justify-center px-10">

                <AddList onAddSuccess={fetchLists}  />


            </SidebarFooter>
        </Sidebar>
    )
}
