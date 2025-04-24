import { Avatar} from "@/components/ui/avatar"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Label } from "@/components/ui/label"
import { Textarea } from "@/components/ui/textarea"



import React, {useRef} from "react";
import { useAuthStore } from "@/storages/auth.ts";
import { cn } from "@/utils";

const MyProfile = () => {
    const user = useAuthStore((state)=>state.user?.username)
    const handleSubmit = async(e:React.FormEvent)=>{
        e.preventDefault()
        /*  */
    }
    const fileInputRef = useRef<HTMLInputElement | null>(null)

    const handleClick = () => {
        fileInputRef.current?.click()
    }

    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files?.[0]
        if (file) {
            console.log("Selected file:", file)
            // 可加入上传逻辑
        }
    }
    return(


        <div className="flex flex-col lg:flex-row gap-5 2xl:mx-60 lg:mx-40 mx-10 mt-16 ">
            {/*
        flex-col md:flex-row
        中等及大屏幕：横向布局。
        小屏幕：头像会居中显示，信息会排成纵向排列。
        mx-60( margin-x轴 左右 )
        mx-auto (auto 代表 自动计算外边距，它会使左右的外边距相等，从而实现居中效果)
      */}
            <aside className="
      lg:sticky lg:top-20 flex flex-col pt-20 w-full h-fit md:basis-1/3
      ">
                {/*
        lg:top-20 是关键，保证往下拉，左侧始终与顶栏差20
        md:mr-5 md:w-75
        当达到中等屏幕以上，侧边栏宽度为父元素的1/4右间距为5 ，
        在小屏幕下默认100%取消右间距
        sticky是对的！
        h-fit 是对的！
      */}
                <div className={"flex flex-row lg:flex-col mb-3 lg:mb-0 mx-3 lg:mx-0"}>
                    <div className={"flex  justify-center"}>{/*justify-center*/}
                        <Avatar src={
                            "https://avatars.githubusercontent.com/u/149759599?v=4"
                        }
                                fallback={"CN"}
                                className=" w-[20vw] h-[20vw] lg:w-75 lg:h-75 overflow-cover "
                        />
                    </div>
                    <div className={"flex flex-col mt-0 lg:mt-5  items-center justify-center pl-5 lg:pl-0"}> {/* items-center不能删*/}
                        <div className={cn(["lg:text-2xl", "text-[32px]", "font-medium"," w-75"])}>{user}</div>
                        <div className={cn(["lg:text-[20px] text-[25px] font-sans text-[#59636e] w-75 italic"])}>Setting </div>
                    </div>
                </div>
                <div className={"flex  mt-3  px-3 justify-center"}> {/*justify-center*/}
                    <input
                        type="file"
                        onChange={handleFileChange}
                        ref={fileInputRef}
                        style={{ display: "none" }} /* 隐藏原生文件选择框 */
                    />
                    <Button onClick={handleClick} className={"lg:w-75 w-full"}>
                        Edit You Avatar
                    </Button>
                </div>
            </aside>


            <Card className="m-3 flex-1 p-6 md:basis-2/3">
                <CardHeader>
                    <CardTitle className={"text-[32px] "}>{user}</CardTitle>
                    <hr className={"my-1 border-gray-300"}/>
                </CardHeader>
                <CardContent>
                    <div className="mb-3">
                        <Label htmlFor="name" className={"text-[20px]"}>username</Label>
                        <br />
                        <br />
                        <br />
                        <br />
                        <br />
                        <br />
                        <br />
                        <br />
                        <br />
                        <br />
                        <br />
                        <br />
                        <br />
                        <br />
                        <br />
                        <br />
                        <br />
                        <br />
                        <br />
                        <br />
                        <br />
                        <br />
                        <br />
                    </div>
                    <form onSubmit={handleSubmit}>
                        <div className="flex flex-col">
                            <Label htmlFor="descrip" className={"text-[20px]"}>Introduce</Label>
                            <hr className={"my-1 border-gray-300"}/>
                            <Textarea placeholder="Introduce youself here"
                                      className={
                                          "mt-2 mb-2 h-100 bg-gray-200 border border-gray-300" +
                                          "focus-visible:ring-2 focus-visible:ring-gray-200/25"

                                      }/>
                            <div className={"flex justify-end"}>
                                <Button type="submit"> SAVE</Button>
                            </div>
                        </div>
                    </form>
                </CardContent>
            </Card>


        </div>
    )

    {/* <div>Welcome Back {nickname} !</div> */}
}

export default MyProfile
