const fs = window.require("fs")
const path = process.env.NODE_ENV === 'production' ? localStorage.getItem('appPath') : process.cwd()

// 读
export function read(fileName) {
    return new Promise((resolve, reject) => {
        fs.readFile(path + fileName, (err, data) => {
            if (err) {
                reject({
                    code: 1,
                    err: err,
                });
            } else {
                resolve({
                    code: 0,
                    data: JSON.parse(data),
                });
            }
        });
    });
}

// 写
export function write(fileName, data) {
    return new Promise((resolve, reject) => {
        fs.writeFile(path + fileName, JSON.stringify(data), (err) => {
            if (err) {
                reject({
                    code: 1,
                    err: err,
                });
            } else {
                resolve({
                    code: 0
                });
            }
        })
    })
}

// 复制
export function copy(srcFileName, destFileName) {
    return new Promise((resolve, reject) => {
        fs.copyFile(path + srcFileName, path + destFileName, (err) => {
            if (err) {
                reject({
                    code: 1,
                    err: err
                })
            } else {
                resolve({
                    code: 0
                })
            }
        })
    })
}