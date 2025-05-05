import { useNavigate } from "react-router-dom";
import { useAuthStore } from "@/storages/auth.ts";
import { useEffect } from "react";

export default function Home() {
    const navigate = useNavigate();
    const user = useAuthStore().user; // 或 useAuth().user

    useEffect(() => {
        if (!user) {
            navigate("/account/login");
        }
    }, [user, navigate]);

    return (
        <>
            {/* 登录用户可见内容 */}
        </>
    );
}