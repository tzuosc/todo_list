import { NavigateFunction } from "react-router";

/* 在非 React 组件外，也能调用路由跳转 */
const globalRouter = { navigate: undefined } as {
    navigate?: NavigateFunction;
};

export default globalRouter;