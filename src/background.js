'use strict'

import { app, protocol, BrowserWindow, screen, ipcMain, dialog, Tray, Menu } from 'electron'
import path from 'path-browserify';
import { createProtocol } from 'vue-cli-plugin-electron-builder/lib'

const isDevelopment = process.env.NODE_ENV !== 'production'
const execPath = process.execPath;

let win = null;
let tray = null;
let checkTimer = null;
let isWindowHidden = false;
let isAnimating = false;
let windowWidth = 350;
let windowHeight = 800;
let savedX = 0;
let savedY = 0;

// 窗口隐藏阈值：窗口顶部距离屏幕上边缘小于此值时触发隐藏
const HIDE_THRESHOLD = 5;
// 窗口显示阈值：鼠标距离屏幕上边缘小于此值时触发显示
const SHOW_THRESHOLD = 30;
// 动画步长：每次动画移动的像素数
const ANIMATION_STEP = 10;

protocol.registerSchemesAsPrivileged([
  { scheme: 'app', privileges: { secure: true, standard: true } }
])

async function createWindow() {
  const primaryDisplay = screen.getPrimaryDisplay();
  const workArea = primaryDisplay.workArea;

  win = new BrowserWindow({
    width: windowWidth,
    height: windowHeight,
    resizable: isDevelopment,
    x: workArea.width - windowWidth,
    y: 0,
    alwaysOnTop: true,
    autoHideMenuBar: true,
    webPreferences: {
      nodeIntegration: true,
      contextIsolation: false,
      webSecurity: false
    }
  })

  // 从实际窗口获取真实尺寸（考虑DPI缩放）
  const bounds = win.getBounds();
  windowWidth = bounds.width;
  windowHeight = bounds.height;

  if (process.env.WEBPACK_DEV_SERVER_URL) {
    await win.loadURL(process.env.WEBPACK_DEV_SERVER_URL)
    if (!process.env.IS_TEST) win.webContents.openDevTools()
  } else {
    createProtocol('app')
    win.loadURL('app://./index.html')
  }

  // 监听窗口移动完成，靠近上边缘时隐藏
  win.on('moved', (event) => {
    if (isWindowHidden) return;
    const [x, y] = win.getPosition();
    if (y <= HIDE_THRESHOLD) {
      hideWindow();
    }
  });

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
    showWindow();
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

/**
 * 窗口向上收起动画（简单步进方式）
 * 将窗口从当前位置向上滚动到屏幕外隐藏
 */
function hideWindow() {
  if (isWindowHidden || isAnimating) return;
  
  const bounds = win.getBounds();
  const startX = Math.floor(bounds.x);
  const startY = Math.floor(bounds.y);
  savedX = startX;
  savedY = startY;
  
  isAnimating = true;
  
  const display = screen.getPrimaryDisplay();
  const workArea = display.workArea;
  const targetY = Math.floor(workArea.y - windowHeight + 10);

  function step() {
    if (!win || win.isDestroyed()) {
      isAnimating = false;
      return;
    }
    
    const currentBounds = win.getBounds();
    const currentY = Math.floor(currentBounds.y);
    
    if (currentY <= targetY) {
      win.setBounds({ x: startX, y: targetY, width: windowWidth, height: windowHeight });
      isWindowHidden = true;
      isAnimating = false;
      startCheckMouse();
      return;
    }
    
    const newY = Math.max(targetY, currentY - ANIMATION_STEP);
    win.setBounds({ x: startX, y: newY, width: windowWidth, height: windowHeight });
    setTimeout(step, 5);
  }
  
  setTimeout(step, 0);
}

/**
 * 窗口向下滑出动画（简单步进方式）
 * 将窗口从屏幕外向下滚动回原始位置显示
 */
function showWindow() {
  if (!isWindowHidden || isAnimating) return;
  
  isAnimating = true;
  stopCheckMouse();

  function step() {
    if (!win || win.isDestroyed()) {
      isAnimating = false;
      return;
    }
    
    const currentBounds = win.getBounds();
    const currentY = Math.floor(currentBounds.y);
    
    if (currentY >= savedY) {
      win.setBounds({ x: savedX, y: savedY, width: windowWidth, height: windowHeight });
      isWindowHidden = false;
      isAnimating = false;
      return;
    }
    
    const newY = Math.min(savedY, currentY + ANIMATION_STEP);
    win.setBounds({ x: savedX, y: newY, width: windowWidth, height: windowHeight });
    setTimeout(step, 5);
  }
  
  setTimeout(step, 0);
}

/**
 * 开始定时检查鼠标位置
 * 当鼠标移动到屏幕上边缘时触发窗口显示
 */
function startCheckMouse() {
  stopCheckMouse();
  checkTimer = setInterval(() => {
    const mousePos = screen.getCursorScreenPoint();
    if (mousePos.y <= SHOW_THRESHOLD) {
      showWindow();
    }
  }, 100);
}

/**
 * 停止检查鼠标位置
 * 清除之前设置的定时器
 */
function stopCheckMouse() {
  if (checkTimer) {
    clearInterval(checkTimer);
    checkTimer = null;
  }
}