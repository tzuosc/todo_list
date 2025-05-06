import { useNavigate } from "react-router-dom";
import { useAuthStore } from "@/storages/auth.ts";
import { useEffect } from "react";
import SplitText from "@/components/ui/SplitText.tsx";
import { cn } from "@/utils";

export default function Home() {
    const navigate = useNavigate();
    const user = useAuthStore().user; // 或 useAuth().user

    const handleAnimationComplete = () => {
        console.log('All letters have animated!');
    };
    useEffect(() => {
        if (!user) {
            navigate("/account/login");
        }
    }, [user, navigate]);

    return (
        <div className={cn(["flex","justify-end"])}>
            {/* 登录用户可见内容 */}
            <div className={cn(["flex","flex-col","w-4/5","justify-center"])}>
                <SplitText
                    text={`Hello,${useAuthStore().user?.username}`}
                    className="text-2xl font-semibold text-center"
                    delay={150}
                    animationFrom={{ opacity: 0, transform: 'translate3d(0,50px,0)' }}
                    animationTo={{ opacity: 1, transform: 'translate3d(0,0,0)' }}
                    easing="easeOutCubic"
                    threshold={0.2}
                    rootMargin="-50px"
                    onLetterAnimationComplete={handleAnimationComplete}
                />
            </div>
        </div>
    );
}