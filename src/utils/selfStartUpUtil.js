import { ipcRenderer } from 'electron'

export function openSelfStartUp() {
    ipcRenderer.send('openSelfStartUp')
}

export function closeSelfStartUp() {
    ipcRenderer.send('closeSelfStartUp')
}