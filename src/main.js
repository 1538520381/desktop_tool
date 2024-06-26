import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import SvgIcon from '@/components/svgIcon'
import '@/assets/icons'
import { ipcRenderer } from 'electron'

createApp(App).use(router).use(ElementPlus).component('svg-icon', SvgIcon).mount('#app')

const debounce = (fn, delay) => {
    let timer = null;
    return function () {
        let context = this;
        let args = arguments;
        clearTimeout(timer);
        timer = setTimeout(function () {
            fn.apply(context, args);
        }, delay);
    }
}

const _ResizeObserver = window.ResizeObserver;
window.ResizeObserver = class ResizeObserver extends _ResizeObserver {
    constructor(callback) {
        callback = debounce(callback, 16);
        super(callback)
    }
}
ipcRenderer.invoke("getAppPath").then((path) => {
    localStorage.setItem("appPath", path.substring(0, path.lastIndexOf('\\')))
});