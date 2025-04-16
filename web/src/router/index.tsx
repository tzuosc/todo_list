import { createBrowserRouter } from "react-router-dom";

export default createBrowserRouter([

    {
        /* hydrateFallback */
        path:"/",
        lazy:async ()=>({
            Component:(await import("@/pages/layout.tsx")).default,
        }),
        children:[
            {
                index:true,
                lazy:async()=>({
                    Component: (
                        await import("@/pages/home")
                    ).default
                })
            },
            {
                path:"account",
                children:[
                    {
                        path:"login",
                        lazy:async ()=>({
                            Component:(
                                await import("@/pages/account/login")
                            ).default
                        })
                    },
                    {
                        path:"register",
                        lazy:async ()=>({
                            Component:(
                                await import("@/pages/account/register")
                            ).default
                        })
                    },
                    {
                        path:"settings",
                        lazy:async ()=>({
                            Component:(
                                await import("@/pages/account/settings")
                            ).default
                        })
                    }
                ]
            },
            {
                path:"/:username",
                lazy:async ()=>({
                    Component:(
                        await import("@/pages/users/profile.tsx")
                    ).default
                })
            },
            {
                path:"/importance",
                lazy:async ()=>({
                    Component:(
                        await import("@/pages/importance")
                    ).default
                })
            },
            {
                path:"/plan",
                lazy:async ()=>({
                    Component:(
                        await import("@/pages/plan")
                    ).default
                })
            }
        ]
    }
])