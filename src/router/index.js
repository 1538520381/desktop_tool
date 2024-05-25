import { createRouter, createWebHistory, createWebHashHistory } from 'vue-router'

const routes = [
    {
        path: '/',
        redirect: '/home'
    },
    {
        path: '/home',
        name: 'home',
        component: () => import('@/views/home')
    },
    {
        path: '/test',
        name: 'test',
        component: () => import('@/views/test')
    }
]

const router = createRouter({
    history: process.env.IS_ELECTRON ? createWebHashHistory(process.env.BASE_URL) : createWebHistory(process.env.BASE_URL),
    routes
});

export default router;