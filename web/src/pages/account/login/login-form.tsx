import { cn } from "@/utils";


import { Input } from "@/components/ui/input.tsx";
import { Label } from "@/components/ui/label.tsx";
import { Button } from "@/components/ui/button.tsx";

function LoginForm(){
    return(
        <form className="px-6 md:px-8">
            <div className="flex flex-col gap-6">
                <div className={cn("flex flex-col items-center text-center")}>
                    <h1 className="text-2xl font-bold">Welcome back</h1>
                    <p className="text-balance text-muted-foreground">Login to your Acme Inc account</p>
                </div>
                <div className="grid gap-2">
                    <Label htmlFor="username">username</Label>
                    <Input id="username"  placeholder="username" />
                </div>
                <div className="grid gap-2">
                    <div className="flex items-center">
                        <Label htmlFor="password">Password</Label>
                        <a href="#" className="ml-auto text-sm underline-offset-2 hover:underline">
                            Forgot your password?
                        </a>
                    </div>
                    <Input id="password" type="password" required />
                </div>
                <Button type="submit" className="w-full">
                    Login
                </Button>


            </div>
        </form>

    )
}
export {LoginForm}