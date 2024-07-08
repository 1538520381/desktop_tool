'use strict'

import { app, protocol, BrowserWindow, screen, ipcMain, dialog, Tray, Menu } from 'electron'
import path from 'path-browserify';
import { createProtocol } from 'vue-cli-plugin-electron-builder/lib'

const isDevelopment = process.env.NODE_ENV !== 'production'
const execPath = process.execPath;

let win = null;
let tray = null;

protocol.registerSchemesAsPrivileged([
  { scheme: 'app', privileges: { secure: true, standard: true } }
])

async function createWindow() {
  win = new BrowserWindow({
    width: 350,
    height: 800,
    resizable: isDevelopment,
    x: screen.getPrimaryDisplay().workArea.width - 350,
    y: 0,
    alwaysOnTop: true,
    webPreferences: {
      nodeIntegration: true,
      contextIsolation: false,
      webSecurity: false
    }
  })

  if (process.env.WEBPACK_DEV_SERVER_URL) {
    await win.loadURL(process.env.WEBPACK_DEV_SERVER_URL)
    if (!process.env.IS_TEST) win.webContents.openDevTools()
  } else {
    createProtocol('app')
    win.loadURL('app://./index.html')
  }

  win.on('close', (event) => {
    win.hide();
    win.setSkipTaskbar(true);
    event.preventDefault();
  });

  if (isDevelopment) {
    tray = new Tray(path.join(__static, './logo/logo.ico'))
  } else {
    tray = new Tray(path.join(__dirname, './logo/logo.ico'))
  }
  tray.setToolTip('desktop_tool')
  tray.on('click', () => {
    win.show()
    win.setSkipTaskbar(false)
  })
  const contectMenu = Menu.buildFromTemplate([
    {
      label: '退出', click: () => {
        win.destroy()
      }
    }
  ])
  tray.setContextMenu(contectMenu)
}

app.on('window-all-closed', () => {
  if (process.platform !== 'darwin') {
    app.quit()
  }
})

app.on('activate', () => {
  if (BrowserWindow.getAllWindows().length === 0) createWindow()
})

app.on('ready', async () => {
  createWindow()
})

if (isDevelopment) {
  if (process.platform === 'win32') {
    process.on('message', (data) => {
      if (data === 'graceful-exit') {
        app.quit()
      }
    })
  } else {
    process.on('SIGTERM', () => {
      app.quit()
    })
  }
}

const gotTheLock = app.requestSingleInstanceLock();
if (!gotTheLock) {
  app.quit();
} else {
  app.on("second-instance", (event, commandLine, workingDirectory) => {
    if (win) {
      if (win.isMinimized()) {
        win.restore()
      }
      win.focus()
    }
  })
}

// 开启开机自启动
ipcMain.on('openSelfStartUp', () => {
  app.setLoginItemSettings({
    openAtLogin: true,
    path: execPath,
    args: []
  });
});

// 关闭开机自启动
ipcMain.on('closeSelfStartUp', () => {
  app.setLoginItemSettings({
    openAtLogin: false,
    path: execPath,
    args: []
  });
});

// 获取软件安装目录
ipcMain.handle('getAppPath', async () => {
  return app.getPath('exe');
})

// 选择目录
ipcMain.handle('selectDirectory', async () => {
  try {
    const result = await dialog.showOpenDialog({
      properties: ["openDirectory"],
    });

    if (!result.canceled) {
      return {
        code: 0,
        path: result.filePaths[0]
      };
    } else {
      return {
        code: 1,
        err: "未选择目录"
      };
    }
  } catch (err) {
    return {
      code: 1,
      err: err.message
    };
  }
})